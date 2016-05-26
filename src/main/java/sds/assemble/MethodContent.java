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

import static sds.util.Utf8ValueExtractor.extract;
import static sds.util.AccessFlags.get;

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
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
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
}