package sds.assemble;

import java.util.Arrays;
import java.util.Iterator;

import sds.assemble.controlflow.CFGBuilder;
import sds.assemble.controlflow.CFNode;
import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.Code;
import sds.classfile.attributes.Code.ExceptionTable;
import sds.classfile.attributes.Exceptions;
import sds.classfile.attributes.LineNumberTable;
import sds.classfile.attributes.LocalVariableTable;
import sds.classfile.attributes.LocalVariableTypeTable;
import sds.classfile.attributes.MethodParameters;
import sds.classfile.attributes.MethodParameters.Parameters;
import sds.classfile.attributes.annotation.AnnotationDefault;
import sds.classfile.attributes.annotation.RuntimeInvisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleParameterAnnotations;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;

import static sds.util.AccessFlags.get;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for contents of method.
 * @author inagaki
 */
public class MethodContent extends MemberContent {
	private String[][] args;
	private String[] exceptions;
	private int maxStack;
	private int maxLocals;
	private LineInstructions[] inst;
	private Opcodes opcodes;
	private ExceptionContent exContent;
	private LocalVariableContent valContent;

	/**
	 * constructor.
	 * @param info method info
	 * @param pool constant-pool
	 */
	public MethodContent(MemberInfo info, ConstantPool pool) {
		super(info, pool);
		System.out.println(this.getName());
		for(AttributeInfo attr : info.getAttr().getAll()) {
			investigateAttribute(attr, pool);
		}
		CFGBuilder builder = CFGBuilder.getInstance();
		CFNode[] nodes = builder.build(inst, exContent);
		for(CFNode n : nodes) {
			System.out.println(n.toString());
		}
		System.out.println("");
	}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
				break;
			case Code:
				Code code = (Code)info;
				this.maxStack    = code.getMaxStack();
				this.maxLocals   = code.maxLocals();
				this.opcodes     = code.getCode();
				ExceptionTable[] exTable = code.getExceptionTable();
				String[] exClass = new String[exTable.length];
				for(int i = 0; i < exTable.length; i++) {
					ExceptionTable t = exTable[i];
					if(t.getNumber("catch_type") == 0) {
						exClass[i] = "any";
					} else {
						exClass[i] = extract(pool.get(t.getNumber("catch_type")), pool);
					}
				}
				this.exContent = new ExceptionContent(exTable, exClass);
				for(AttributeInfo ai : code.getAttr().getAll()) {
					investigateAttribute(ai, pool);
				}
				break;
			case Exceptions:
				int[] exp = ((Exceptions)info).getExceptionIndexTable();
				this.exceptions = new String[exp.length];
				for(int i = 0; i < exp.length; i++) {
					exceptions[i] = extract(pool.get(exp[i]-1), pool).replace("/", ".");
				}
				break;
			case LineNumberTable:
				LineNumberTable.LNTable[] table = ((LineNumberTable)info).getLineNumberTable();
				this.inst = new LineInstructions[table.length];
				for(int i = 0; i < inst.length; i++) {
					inst[i] = new LineInstructions(table[i]);
				}
				Iterator<OpcodeInfo> itr = Arrays.asList(opcodes.getAll()).iterator();
				if(inst.length == 1) {
					while(itr.hasNext()) {
						inst[0].addOpcode(itr.next());
					}
				} else {
					int index = 0;
					while(itr.hasNext()) {
						OpcodeInfo op = itr.next();
						if(op.getPc() < table[index].getEndPc()) {
							inst[index].addOpcode(op);
						} else { // shift next line
							index++;
							if(index < inst.length) {
								inst[index].addOpcode(op);
							} else {
								// when end line of method has some instructions,
								// it adds to end instruction in the line
								// because the line doesn't have the instruction.
								if(inst[index-1].getOpcodes().get(op.getPc()) == null) {
									inst[index-1].addOpcode(op);
								}
								break;
							}
						}
					}
				}
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable)info;
				this.valContent = new LocalVariableContent(lvt.getTable(), pool);
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
				valContent.setType(lvtt.getTable(), pool);
				break;
			case MethodParameters:
				Parameters[] param = ((MethodParameters)info).getParams();
				this.args = new String[param.length][2];
				for(int i = 0; i < param.length; i++) {
					args[i][0] = get(param[i].getAccessFlag(), "local");
					args[i][1] = extract(pool.get(param[i].getNameIndex()-1), pool);
				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa
					= (RuntimeInvisibleParameterAnnotations)info;
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa
					= (RuntimeVisibleParameterAnnotations)info;
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				break;
			default:
				super.investigateAttribute(info, pool);
				break;
		}
	}

	
	/***** Nested Classes *****/

	/**
	 * This class is for contents of method's try-catch-finally statement.
	 */
	public class ExceptionContent {
		private int[] from, to, target;
		private String[] exception;

		ExceptionContent(ExceptionTable[] table, String[] exception) {
			this.from   = new int[table.length];
			this.to     = new int[table.length];
			this.target = new int[table.length];
			for(int i = 0; i < table.length; i++) {
				from[i]   = table[i].getNumber("start_pc");
				to[i]     = table[i].getNumber("end_pc");
				target[i] = table[i].getNumber("handler_pc");
			}
			this.exception = exception;
		}

		/**
		 * returns from-indexes into code array.
		 * @return from-indexes
		 */
		public int[] getFrom() {
			return from;
		}

		/**
		 * returns to-indexes into code array.
		 * @return to-indexes
		 */
		public int[] getTo() {
			return to;
		}

		/**
		 * returns target indexes into code array.
		 * @return target indexes
		 */
		public int[] getTarget() {
			return target;
		}

		/**
		 * returns exception class names.
		 * @return exception class names
		 */
		public String[] getException() {
			return exception;
		}

		/**
		 * returns array indexes which specified pc is
		 * in range between from_index and to_index (or to_index-1).<br>
		 * if the array index didn't find, this method returns empty array.
		 * @param pc index into code array
		 * @param isAny whether the range has no exception
		 * @param gotoStart whether start instruction of node is goto
		 * @return array indexes
		 */
		public int[] getIndexInRange(int pc, boolean isAny, boolean gotoStart) {
			int range = 0;
			int[] indexes = new int[from.length];
			for(int i = 0; i < from.length; i++) {
				if(gotoStart) {
					if(from[i] <= pc && pc <= to[i]) {
						if(isAny) {
							if(exception[i].equals("any"))   indexes[range++] = i;
						} else {
							if(! exception[i].equals("any")) indexes[range++] = i;
						}
					}
				} else {
					if(from[i] <= pc && pc < to[i]) {
						if(isAny) {
							if(exception[i].equals("any"))   indexes[range++] = i;
						} else {
							if(! exception[i].equals("any")) indexes[range++] = i;
						}
					}
				}
			}
			return (range != 0) ? Arrays.copyOf(indexes, range) : new int[0];
		}
	}

	/**
	 * This class is for local variables in method.
	 */
	public class LocalVariableContent {
		private int[][] range;
		private String[][] variable;
		private int[] index;
		private boolean hasValType;

		LocalVariableContent(LocalVariableTable.LVTable[] table, ConstantPool pool) {
			this.range = new int[table.length][2];
			this.variable = new String[table.length][2];
			this.index = new int[table.length];
			this.hasValType = false;
			int i = 0;
			for(LocalVariableTable.LVTable t: table) {
				range[i][0] = t.getNumber("start_pc");
				range[i][1] = t.getNumber("length") + range[i][0];
				variable[i][0] = extract(pool.get(t.getNumber("name_index")-1), pool);
				variable[i][1] = extract(pool.get(t.getNumber("descriptor")-1), pool);
				index[i] = t.getNumber("index");
				i++;
			}
		}

		void setType(LocalVariableTypeTable.LVTable[] table, ConstantPool pool) {
			this.hasValType = true;
			for(LocalVariableTypeTable.LVTable t : table) {
				int lvIndex = t.getNumber("index");
				for(int i = 0; i < index.length; i++) {
					if(lvIndex == index[i]) {
						String desc = extract(pool.get(t.getNumber("descriptor")-1), pool);
						String genericsType = desc.substring(desc.indexOf("<")+1, desc.indexOf(">"));
						variable[i][1] += "," + parse(genericsType);
						break;
					}
				}
			}
		}

		/**
		 * returns valid ranges of variable.
		 * @return 
		 */
		public int[][] getRanges() {
			return range;
		}

		/**
		 * returns valid range of variable.
		 * @param index 
		 * @return 
		 */
		public int[] getRange(int index) {
			for(int i = 0; i < this.index.length; i++) {
				if(index == this.index[i]) {
					return range[i];
				}
			}
			throw new NotFoundSpecifiedIndex(index);
		}

		/**
		 * returns variables.
		 * @return 
		 */
		public String[][] getVariables() {
			return variable;
		}

		/**
		 * returns variable of specified index.
		 * @param index index
		 * @return variable
		 */
		public String[] getVariable(int index) {
			for(int i = 0; i < this.index.length; i++) {
				if(index == this.index[i]) {
					return variable[i];
				}
			}
			throw new NotFoundSpecifiedIndex(index);
		}

		/**
		 * returns variable indexes.
		 * @return variable indexes
		 */
		public int[] getIndexes() {
			return index;
		}

		/**
		 * returns variable index of specified index.
		 * @param index index
		 * @return variable index
		 */
		public int getIndex(int index) {
			for(int i = 0; i < this.index.length; i++) {
				if(index == this.index[i]) {
					return this.index[i];
				}
			}
			throw new NotFoundSpecifiedIndex(index);
		}

		/**
		 * return whether the method has
		 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.14">
		 * LocalVariableTypeTable Attribute</a>.
		 * @return if the method has the attribute, this method returns true.<br>
		 * Otherwise, it returns false.
		 */
		public boolean hasValType() {
			return hasValType;
		}

		class NotFoundSpecifiedIndex extends RuntimeException {
			NotFoundSpecifiedIndex(int index) {
				super("not found the specified index: " + index);
			}
		}
	}
}