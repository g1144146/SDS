package sds.assemble;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import sds.classfile.attributes.annotation.CatchTarget;
import sds.classfile.attributes.annotation.ParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.TargetInfo;
import sds.classfile.attributes.annotation.ThrowsTarget;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;

import static sds.util.DescriptorParser.parse;

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
	private ParamAnnotationContent paContent;
	private String defaultAnn;

	/**
	 * constructor.
	 * @param info method info
	 * @param pool constant-pool
	 */
	public MethodContent(MemberInfo info, ConstantPool pool) {
		super(info, pool);
		System.out.println(this.getName());
		// set arguments
		String desc = this.desc;
		if(desc.indexOf("(") + 1 == desc.indexOf(")")) { // has argument
			String arg = desc.substring(desc.indexOf("(") + 1, desc.indexOf(")"));
			if(arg.contains(",")) {
				String[] argArray = arg.split(",");
				this.args = new String[argArray.length][2];
				for(int i = 0; i < argArray.length; i++) {
					this.args[i][0] = argArray[i];
					this.args[i][1] = "arg" + String.valueOf(i);
				}
			} else {
				this.args = new String[][]{{arg, "arg0"}};
			}
		}
		// attriutes
		for(AttributeInfo attr : info.getAttr().getAll()) {
			investigateAttribute(attr, pool);
		}
		// print
		if(valContent != null) {
			System.out.println("[local variable]");
			System.out.println(valContent);
		}
		if(exContent != null && exContent.from.length > 0) {
			System.out.println("[exception]");
			System.out.println(exContent);
		}
		if(args != null) {
			for(String[] arg : args) {
				System.out.println(arg[0] + " " + arg[1]);
			}
		}
		// set CFG
		if(!this.getAccessFlag().contains("abstract")) {
			CFGBuilder builder = CFGBuilder.getInstance();
			CFNode[] nodes = builder.build(inst, exContent);
			for(CFNode n : nodes) {
				System.out.println(n.toString());
			}
		}
		System.out.println("");
	}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault) info;
				this.defaultAnn = ad.getDefaultValue();
				break;
			case Code:
				Code code = (Code) info;
				this.maxStack = code.getMaxStack();
				this.maxLocals = code.maxLocals();
				this.opcodes = code.getCode();
				// throws exceptions
				ExceptionTable[] exTable = code.getExceptionTable();
				String[] exClass = new String[exTable.length];
				for(int i = 0; i < exTable.length; i++) {
					exClass[i] = exTable[i].getCatchType();
				}
				this.exContent = new ExceptionContent(exTable, exClass);
				// other attributes
				for(AttributeInfo ai : code.getAttr().getAll()) {
					investigateAttribute(ai, pool);
				}
				break;
			case Exceptions:
				this.exceptions = ((Exceptions) info).getExceptionTable();
				break;
			case LineNumberTable:
				LineNumberTable.LNTable[] table = ((LineNumberTable) info).getLineNumberTable();
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
								if(inst[index - 1].getOpcodes().get(op.getPc()) == null) {
									inst[index - 1].addOpcode(op);
								}
								break;
							}
						}
					}
				}
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable) info;
				this.valContent = new LocalVariableContent(lvt.getTable(), pool);
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable) info;
				valContent.setType(lvtt.getTable());
				break;
			case MethodParameters:
				Parameters[] param = ((MethodParameters) info).getParams();
				this.args = new String[param.length][2];
				for(int i = 0; i < param.length; i++) {
					args[i][0] = param[i].getAccessFlag();
					args[i][1] = param[i].getName();
				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa = (RuntimeInvisibleParameterAnnotations) info;
				ParameterAnnotations[] invisiblePA = ripa.getParamAnnotations();
				if(paContent == null) {
					this.paContent = new ParamAnnotationContent(invisiblePA, false);
				} else {
					paContent.setInvisible(invisiblePA);
				}
//				System.out.println("<<<Runtime Invisible Parameter Annotation>>>: ");
//				for(String[] a : paContent.invParam)
//					System.out.println("  " + Arrays.toString(a));
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa = (RuntimeVisibleParameterAnnotations) info;
				ParameterAnnotations[] visiblePA = rvpa.getParamAnnotations();
				this.paContent = new ParamAnnotationContent(visiblePA, true);
//				System.out.println("<<<Runtime Visible Parameter Annotation>>>: ");
//				for(String[] a : paContent.param)
//					System.out.println("  " + Arrays.toString(a));
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations) info;
				this.taContent = new MethodTypeAnnotationContent(rvta.getAnnotations(), true);
				System.out.println("<<<Runtime Visible Type Annotation>>>: ");
				for(int i = 0; i < taContent.count; i++) {
					System.out.print(taContent.visible[i]);
					System.out.println(", " + taContent.targets[i]);
				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations) info;
				if(taContent == null) {
					this.taContent = new MethodTypeAnnotationContent(rita.getAnnotations(), false);
				} else {
					taContent.setInvisible(rita.getAnnotations());
				}
				System.out.println("<<<Runtime Invisible Type Annotation>>>: ");
				for(int i = 0; i < taContent.count; i++) {
					System.out.print(taContent.invisible[i]);
					System.out.println(", " + taContent.invTargets[i]);
				}
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable) info;
				break;
			default:
				super.investigateAttribute(info, pool);
				break;
		}
	}

	/**
	 * returns exception content of this method.
	 * @return excpetion content
	 */
	public ExceptionContent getExContent() {
		return exContent;
	}

	/**
	 * returns local variable content of this method.
	 * @return local variable content
	 */
	public LocalVariableContent getValContent() {
		return valContent;
	}

	/**
	 * returns parameter annotation content of this method.
	 * @return parameter annotation content
	 */
	public ParamAnnotationContent getParamAnnotation() {
		return paContent;
	}

	/**
	 * returns default value of annotation interface's method.<br>
	 * when the method is not annotation interface's or default value is undefine , this method returns null.
	 *
	 * @return default value
	 */
	public String getDefaultAnn() {
		return defaultAnn;
	}

	// <editor-fold defaultstate="collapsed" desc="[class] ExceptionContent">
	/**
	 * This class is for contents of method's try-catch-finally statement.
	 */
	public class ExceptionContent {
		private int[] from, to, target;
		private String[] exception;

		ExceptionContent(ExceptionTable[] table, String[] exception) {
			this.from = new int[table.length];
			this.to = new int[table.length];
			this.target = new int[table.length];
			for(int i = 0; i < table.length; i++) {
				from[i] = table[i].getNumber("start_pc");
				to[i] = table[i].getNumber("end_pc");
				target[i] = table[i].getNumber("handler_pc");
			}
			this.exception = exception;
		}

		/**
		 * returns from-indexes into code array.
		 * @return from-indexes
		 */
		public int[] getFrom() {
			return from != null ? from : new int[0];
		}

		/**
		 * returns to-indexes into code array.
		 * @return to-indexes
		 */
		public int[] getTo() {
			return to != null ? to : new int[0];
		}

		/**
		 * returns target indexes into code array.
		 * @return target indexes
		 */
		public int[] getTarget() {
			return target != null ? target : new int[0];
		}

		/**
		 * returns exception class names.
		 * @return exception class names
		 */
		public String[] getException() {
			return exception != null ? exception : new String[0];
		}

		/**
		 * returns array indexes which specified pc is in range between from_index and to_index (or
		 * to_index-1).<br>
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
							if(exception[i].equals("any")) {
								indexes[range++] = i;
							}
						} else {
							if(! exception[i].equals("any")) {
								indexes[range++] = i;
							}
						}
					}
				} else {
					if(from[i] <= pc && pc < to[i]) {
						if(isAny) {
							if(exception[i].equals("any")) {
								indexes[range++] = i;
							}
						} else {
							if(!exception[i].equals("any")) {
								indexes[range++] = i;
							}
						}
					}
				}
			}
			return (range != 0) ? Arrays.copyOf(indexes, range) : new int[0];
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < from.length - 1; i++) {
				sb.append(from[i]).append("-").append(to[i])
						.append(", ").append(target[i]).append(", ")
						.append(exception[i]).append(System.getProperty("line.separator"));
			}
			sb.append(from[from.length - 1]).append("-").append(to[from.length - 1])
					.append(", ").append(target[from.length - 1]).append(", ")
					.append(exception[from.length - 1]);
			return sb.toString();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="[class] LocalVariableContent">
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
			for(LocalVariableTable.LVTable t : table) {
				range[i][0] = t.getNumber("start_pc");
				range[i][1] = t.getNumber("length") + range[i][0];
				variable[i][0] = t.getName();
				variable[i][1] = t.getDesc();
				index[i] = t.getNumber("index");
				i++;
			}
		}

		void setType(LocalVariableTypeTable.LVTable[] table) {
			this.hasValType = true;
			for(LocalVariableTypeTable.LVTable t : table) {
				int lvIndex = t.getNumber("index");
				for(int i = 0; i < index.length; i++) {
					if(lvIndex == index[i]) {
						String desc = t.getDesc();
						String valType = parse(desc);
						if(valType.contains("<")) {
							variable[i][1] += valType.substring(valType.indexOf("<"));
						} else {
							variable[i][1] = valType;
						}
						break;
					}
				}
			}
		}

		/**
		 * returns valid ranges of variable.<br>
		 * returned array: int[variable_count][2]<br>
		 * int[variable_count][0]: start pc<br>
		 * int[variable_count][1]: end pc
		 *
		 * @return ranges
		 */
		public int[][] getRanges() {
			return range;
		}

		/**
		 * returns variables.<br>
		 * returned array: String[variable_count][2]<br>
		 * String[variable_count][0]: variable name<br>
		 * String[variable_count][1]: variable descriptor
		 *
		 * @return variables
		 */
		public String[][] getVariables() {
			return variable;
		}

		/**
		 * returns variable indexes.
		 *
		 * @return variable indexes
		 */
		public int[] getIndexes() {
			return index;
		}

		/**
		 * return whether the method has
		 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.14">
		 * LocalVariableTypeTable Attribute</a>.
		 *
		 * @return if the method has the attribute, this method returns true.<br>
		 * Otherwise, it returns false.
		 */
		public boolean hasValType() {
			return hasValType;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < range.length - 1; i++) {
				sb.append(range[i][0]).append("-").append(range[i][1])
						.append(", ").append(index[i]).append(", ")
						.append(variable[i][1]).append(" ").append(variable[i][0])
						.append(System.getProperty("line.separator"));
			}
			sb.append(range[range.length - 1][0]).append("-").append(range[range.length - 1][1])
					.append(", ").append(index[range.length - 1]).append(", ")
					.append(variable[range.length - 1][1]).append(" ").append(variable[range.length - 1][0]);
			return sb.toString();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="[class] ParamAnnotationContent">
	/**
	 * This class is for annotations of method parameters.
	 */
	public class ParamAnnotationContent extends AnnotationContent {

		private List<String[]> param;
		private List<String[]> invParam;
		private int paramCount;

		ParamAnnotationContent(ParameterAnnotations[] pa, boolean isVisible) {
			super(isVisible);
			this.paramCount = pa.length;
			initAnnotation(pa, isVisible);
		}

		private void setInvisible(ParameterAnnotations[] annotations) {
			type = type | INVISIBLE;
			initAnnotation(annotations, false);
		}

		private void initAnnotation(ParameterAnnotations[] pa, boolean isVisible) {
			if(isVisible) {
				param = new ArrayList<>();
			} else {
				invParam = new ArrayList<>();
			}
			for(int i = 0; i < paramCount; i++) {
				super.initAnnotation(pa[i].getAnnotations(), isVisible);
				if(isVisible) {
					param.add(visible);
				} else {
					invParam.add(invisible);
				}
			}
		}

		/**
		 * returns parameter annotations.
		 *
		 * @param isVisible whether runtime visible annotation
		 * @return parameter annotations
		 */
		public List<String[]> getParams(boolean isVisible) {
			return isVisible ? param : invParam;
		}

		/**
		 * returns parameter annotation of specified array index.
		 *
		 * @param index array index
		 * @param isVisible whether runtime visible annotation
		 * @return parameter annotation
		 */
		public String[] getParam(int index, boolean isVisible) {
			if(0 <= index && index <= paramCount) {
				return isVisible ? param.get(index) : invParam.get(index);
			}
			throw new IndexOutOfBoundsException("" + index);
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="[class] MethodTypeAnnotationContent">
	/**
	 * This class is {@link TypeAnnotationContent <code>TypeAnnotationContent</code>} for method.
	 */
	public class MethodTypeAnnotationContent extends TypeAnnotationContent {

		MethodTypeAnnotationContent(String[] ta, boolean isVisible) {
			super(ta, isVisible);
		}

		@Override
		void initTarget(TargetInfo target, int annIndex, boolean isVisible) {
			String annotation = isVisible ? visible[annIndex] : invisible[annIndex];
			switch(target.getType()) {
				case CatchTarget:
					CatchTarget ct = (CatchTarget) target;
					exContent.getException()[ct.getIndex()]
							= annotation + " " + exContent.getException()[ct.getIndex()];
					break;
				case LocalVarTarget:
//					LocalVarTarget.LVTTable table = ((LocalVarTarget)target).getTable()[0];
//					sb.append(",").append(table.getStartPc())
//						.append("-").append(table.getStartPc() + table.getLen())
//						.append(",").append(table.getIndex());
//					if(valContent != null) {
//						int index = table.getIndex();
//						int[] indexes = valContent.getIndexes();
//						for(int i = 0; i < indexes.length; i++) {
//							if(index == indexes[i]) {
//								valContent.getVariables()[i][1]
//									= annotation + " " + valContent.getVariables()[i][1];
//								sb.append(valContent.getVariables()[i][1])
//									.append(" ").append(valContent.getVariables()[i][0]);
//								break;
//							}
//						}
//					}
					break;
				case MethodFormalParameterTarget:
//					MethodFormalParameterTarget mfpt = (MethodFormalParameterTarget)target;
//					if(args != null) {
//						args[mfpt.getIndex()][0] = annotation + " " + args[mfpt.getIndex()][0];
//						sb.append(args[mfpt.getIndex()][0]).append(args[mfpt.getIndex()][1]);
//					} else {
//						String desc = getDescriptor();
//						String methodArg = desc.substring(desc.indexOf("(") + 1, desc.indexOf(")"));
//						if(methodArg.contains(",")) {
//							sb.append(",").append(methodArg.split(",")[mfpt.getIndex()]);
//						} else {
//							sb.append(",").append(methodArg);
//						}
//					}
					break;
				case OffsetTarget:
//					OffsetTarget ot = (OffsetTarget)target;
//					sb.append(",").append(ot.getOffset());
					break;
				case ThrowsTarget:
					ThrowsTarget tt = (ThrowsTarget) target;
					exceptions[tt.getIndex()] = annotation + " " + exceptions[tt.getIndex()];
					break;
				case TypeParameterTarget:
//					TypeParameterTarget tpt = (TypeParameterTarget)target;
					break;
				case TypeParameterBoundTarget:
//					TypeParameterBoundTarget tpbt = (TypeParameterBoundTarget)target;
					break;
			}
		}
	}
	// </editor-fold>
}
