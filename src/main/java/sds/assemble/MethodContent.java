package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.Code;
import sds.classfile.attributes.EnclosingMethod;
import sds.classfile.attributes.Exceptions;
import sds.classfile.attributes.LineNumberTable;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.util.Utf8ValueExtractor;

/**
 *
 * @author inagaki
 */
public class MethodContent extends MemberContent {
	String args;
	String[] exceptions;
	int maxStack;
	int maxLocals;
	LineInstructions[] inst;
	Opcodes opcodes;

	public MethodContent(MemberInfo info, ConstantPool pool) {
		super(info, pool);
		for(AttributeInfo attr : info.getAttr().getAll()) {
			investigateAttribute(attr, pool);
		}
	}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		super.investigateAttribute(info, pool);
		switch(info.getType()) {
			case Code:
				Code code = (Code)info;
				this.maxStack  = code.getMaxStack();
				this.maxLocals = code.maxLocals();
				this.opcodes = code.getCode();
//				code.getExceptionTable();
				for(AttributeInfo ai : code.getAttr().getAll()) {
					investigateAttribute(ai, pool);
				}
				break;
			case EnclosingMethod:
				EnclosingMethod em = (EnclosingMethod)info;
				break;
			case Exceptions:
				int[] exp = ((Exceptions)info).getExceptionIndexTable();
				this.exceptions = new String[exp.length];
				for(int i = 0; i < exp.length; i++) {
					exceptions[i] = Utf8ValueExtractor.extract(pool.get(exp[i]-1), pool)
									.replace("/", ".");
				}
				break;
			case LineNumberTable:
				LineNumberTable.LNTable[] table = ((LineNumberTable)info).getLineNumberTable();
				this.inst = new LineInstructions[table.length];
				int index = 0;
				for(OpcodeInfo op : opcodes.getAll()) {
					if(op.getPc() != table[index].getEndPc()) {
						inst[index].addOpcode(op);
					} else {
						index++;
					}
				}
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				break;
		}
	}
}
