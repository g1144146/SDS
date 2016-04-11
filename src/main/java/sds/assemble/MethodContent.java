package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.Code;
import sds.classfile.attributes.EnclosingMethod;
import sds.classfile.attributes.Exceptions;
import sds.classfile.attributes.LineNumberTable;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.Opcodes;
import sds.util.ClassFilePrinter;

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
	}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		super.investigateAttribute(info, pool);
		ClassFilePrinter cfp = new ClassFilePrinter();
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
//				if(checkRange(em.classIndex()-1)) {
//					out.println("\t" + getUtf8Value(pool.get(em.classIndex()-1)));
//				}
//				if(checkRange(em.methodIndex()-1)) {
//					out.println("\t" + getUtf8Value(pool.get(em.methodIndex()-1)));
//				}
				break;
			case Exceptions:
				int[] exp = ((Exceptions)info).getExceptionIndexTable();
				this.exceptions = new String[exp.length];
				for(int i = 0; i < exp.length; i++) {
					exceptions[i] = cfp.getUtf8Value(pool.get(exp[i]-1)).replace("/", ".");
				}
				break;
			case LineNumberTable:
				LineNumberTable.LNTable[] table = ((LineNumberTable)info).getLineNumberTable();
				this.inst = new LineInstructions[table.length];
				for(int i = 0; i < inst.length; i++) {
					inst[i] = new LineInstructions(table[i]);
				}
				break;
			case StackMapTable:
				StackMapTable smt = (StackMapTable)info;
				break;
		}
	}
}
