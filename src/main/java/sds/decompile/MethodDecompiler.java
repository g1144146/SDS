package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.LineInstructions;
import sds.assemble.MethodContent;
import sds.assemble.controlflow.CFNode;
import sds.classfile.bytecode.CpRefOpcode;
import sds.classfile.bytecode.IndexOpcode;
import sds.classfile.bytecode.MultiANewArray;
import sds.classfile.bytecode.NewArray;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.PushOpcode;

import static sds.classfile.bytecode.MnemonicTable.*;

/**
 * This class is for decompiling contents of method.
 * @author inagaki
 */
public class MethodDecompiler extends AbstractDecompiler {
	private OperandStack opStack;
	private LocalStack local;

	/**
	 * constructor.
	 * @param result decompiled source
	 */
	public MethodDecompiler(DecompiledResult result) {
		super(result);
	}

	@Override
	public void decompile(BaseContent content) {
		MethodContent method = (MethodContent)content;
		this.opStack = new OperandStack(method.getMaxStack());
		this.local = new LocalStack(method.getMaxLocals());
		addAnnotation(method.getAnnotation());
		addDeclaration(method);
		result.changeIndent(DecompiledResult.INCREMENT);
		result.writeEndScope();
	}

	@Override
	void addDeclaration(BaseContent content) {
		MethodContent method = (MethodContent)content;
		StringBuilder methodDeclaration = new StringBuilder();
		methodDeclaration.append(method.getAccessFlag()).append(method.getDescriptor())
						.append(" ").append(method.getName()).append("(");
		// args
		String[][] args = method.getArgs();
		for(int i = 0; i < args.length - 1 ; i++) {
			methodDeclaration.append(args[i][0]).append(" ").append(args[i][1]).append(", ");
		}
		methodDeclaration.append(args[args.length - 1][0])
						.append(" ").append(args[args.length - 1][1]).append(")");
		// abstract method
		if(method.getAccessFlag().contains("abstract")) {
			methodDeclaration.append(";");
		// the other, and has throws statement
		} else {
			if(method.getExceptions().length > 0) {
				methodDeclaration.append(" throws ");
				String[] exceptions = method.getExceptions();
				for(int i = 0; i < exceptions.length - 1; i++) {
					methodDeclaration.append(exceptions[i]).append(", ");
				}
				methodDeclaration.append(exceptions[exceptions.length - 1]);
			}
			methodDeclaration.append(" {");
		}
		result.write(methodDeclaration.toString());
	}

	private void examineOpcode(OpcodeInfo opcode) {
		switch(opcode.getOpcodeType()) {
			case nop:
				break;
			case aconst_null:
				opStack.push("null");
				break;
			case iconst_m1:
				opStack.push(-1);
				break;
			case iconst_0:
				opStack.push(0);
				break;
			case iconst_1:
				opStack.push(1);
				break;
			case iconst_2:
				opStack.push(2);
				break;
			case iconst_3:
				opStack.push(3);
				break;
			case iconst_4:
				opStack.push(4);
				break;
			case iconst_5:
				opStack.push(5);
				break;
			case lconst_0:
				opStack.push(0L);
				break;
			case lconst_1:
				opStack.push(1L);
				break;
			case fconst_0:
				opStack.push(0.0f);
				break;
			case fconst_1:
				opStack.push(1.0f);
				break;
			case fconst_2:
				opStack.push(2.0f);
				break;
			case dconst_0:
				opStack.push(0.0d);
				break;
			case dconst_1:
				opStack.push(1.0d);
				break;
			case bipush:
			case sipush:
				opStack.push(((PushOpcode)opcode).getValue());
				break;
			case ldc:
			case ldc_w:
			case ldc2_w:
				opStack.push(((CpRefOpcode)opcode).getOperand());
				break;
			case iload:
			case lload:
			case fload:
			case dload:
			case aload:
				String loaded = local.load(((IndexOpcode)opcode).getIndex());
				opStack.push(loaded);
				break;
			case iload_0:
				String i_0 = local.load(0);
				opStack.push(i_0);
				break;
			case iload_1:
				String i_1 = local.load(1);
				opStack.push(i_1);
				break;
			case iload_2:
				String i_2 = local.load(2);
				opStack.push(i_2);
				break;
			case iload_3:
				String i_3 = local.load(3);
				opStack.push(i_3);
				break;
			case lload_0:
				String l_0 = local.load(0);
				opStack.push(l_0);
				break;
			case lload_1:
				String l_1 = local.load(1);
				opStack.push(l_1);
				break;
			case lload_2:
				String l_2 = local.load(2);
				opStack.push(l_2);
				break;
			case lload_3:
				String l_3 = local.load(3);
				opStack.push(l_3);
				break;
			case fload_0:
				String f_0 = local.load(0);
				opStack.push(f_0);
				break;
			case fload_1:
				String f_1 = local.load(1);
				opStack.push(f_1);
				break;
			case fload_2:
				String f_2 = local.load(2);
				opStack.push(f_2);
				break;
			case fload_3:
				String f_3 = local.load(3);
				opStack.push(f_3);
				break;
			case dload_0:
				String d_0 = local.load(0);
				opStack.push(d_0);
				break;
			case dload_1:
				String d_1 = local.load(1);
				opStack.push(d_1);
				break;
			case dload_2:
				String d_2 = local.load(2);
				opStack.push(d_2);
				break;
			case dload_3:
				String d_3 = local.load(3);
				opStack.push(d_3);
				break;
			case aload_0:
				String a_0 = local.load(0);
				opStack.push(a_0);
				break;
			case aload_1:
				String a_1 = local.load(1);
				opStack.push(a_1);
				break;
			case aload_2:
				String a_2 = local.load(2);
				opStack.push(a_2);
				break;
			case aload_3:
				String a_3 = local.load(3);
				opStack.push(a_3);
				break;
			case iaload:
			case laload:
			case faload:
			case daload:
			case aaload:
			case baload:
			case caload:
			case saload:
				String arrayIndex = opStack.pop();
				String refedArray = opStack.pop();
				opStack.push(refedArray + "[" + arrayIndex + "]");
				break;
			case istore: break;
			case lstore: break;
			case fstore: break;
			case dstore: break;
			case astore: break;
			case istore_0: break;
			case istore_1: break;
			case istore_2: break;
			case istore_3: break;
			case lstore_0: break;
			case lstore_1: break;
			case lstore_2: break;
			case lstore_3: break;
			case fstore_0: break;
			case fstore_1: break;
			case fstore_2: break;
			case fstore_3: break;
			case dstore_0: break;
			case dstore_1: break;
			case dstore_2: break;
			case dstore_3: break;
			case astore_0: break;
			case astore_1: break;
			case astore_2: break;
			case astore_3: break;
			case iastore: break;
			case lastore: break;
			case fastore: break;
			case dastore: break;
			case aastore: break;
			case bastore: break;
			case castore: break;
			case sastore: break;
			case pop:
				opStack.pop();
				break;
			case pop2:
				opStack.pop();
				opStack.pop();
				break;
			case dup:
				String dup = opStack.pop();
				opStack.push(dup);
				opStack.push(dup);
				break;
			case dup_x1:
				String dup_x1_2 = opStack.pop();
				String dup_x1_1 = opStack.pop();
				opStack.push(dup_x1_1);
				opStack.push(dup_x1_2);
				opStack.push(dup_x1_1);
				break;
			case dup_x2:
				String dup_x2_3 = opStack.pop();
				String dup_x2_2 = opStack.pop();
				String dup_x2_1 = opStack.pop();
				opStack.push(dup_x2_1);
				opStack.push(dup_x2_2);
				opStack.push(dup_x2_3);
				opStack.push(dup_x2_1);
				break;
			case dup2:
				String dup2_1 = opStack.pop();
				String dup2_2 = opStack.pop();
				opStack.push(dup2_2);
				opStack.push(dup2_1);
				opStack.push(dup2_2);
				opStack.push(dup2_1);
				break;
			case dup2_x1:
				String dup2_x1_3 = opStack.pop();
				String dup2_x1_2 = opStack.pop();
				String dup2_x1_1 = opStack.pop();
				opStack.push(dup2_x1_1);
				opStack.push(dup2_x1_2);
				opStack.push(dup2_x1_3);
				opStack.push(dup2_x1_1);
				opStack.push(dup2_x1_2);
				break;
			case dup2_x2:
				String dup2_x2_4 = opStack.pop();
				String dup2_x2_3 = opStack.pop();
				String dup2_x2_2 = opStack.pop();
				String dup2_x2_1 = opStack.pop();
				opStack.push(dup2_x2_1);
				opStack.push(dup2_x2_2);
				opStack.push(dup2_x2_3);
				opStack.push(dup2_x2_4);
				opStack.push(dup2_x2_1);
				opStack.push(dup2_x2_2);
				break;
			case swap:
				String two = opStack.pop();
				String one = opStack.pop();
				opStack.push(two);
				opStack.push(one);
				break;
			case iadd:
			case ladd:
			case fadd:
			case dadd:
				String sum =  "(" + opStack.pop() + " + " + opStack.pop() + ")";
				opStack.push(sum);
				break;
			case isub:
			case lsub:
			case fsub:
			case dsub:
				String sub =  "(" + opStack.pop() + " - " + opStack.pop() + ")";
				opStack.push(sub);
				break;
			case imul:
			case lmul:
			case fmul:
			case dmul:
				String product =  "(" + opStack.pop() + " * " + opStack.pop() + ")";
				opStack.push(product);
				break;
			case idiv:
			case ldiv:
			case fdiv:
			case ddiv:
				String quotient =  "(" + opStack.pop() + " / " + opStack.pop() + ")";
				opStack.push(quotient);
				break;
			case irem:
			case lrem:
			case frem:
			case drem:
				String remainder =  "(" + opStack.pop() + " % " + opStack.pop() + ")";
				opStack.push(remainder);
				break;
			case ineg:
			case lneg:
			case fneg:
			case dneg:
				String minus = "-(" + opStack.pop() + ")";
				opStack.push(minus);
				break;
			case ishl:
			case lshl:
				String right_1 = opStack.pop();
				String left_1 = opStack.pop();
				opStack.push("(" + left_1 + " << " + right_1 + ")");
				break;
			case ishr:
			case lshr:
				String right_2 = opStack.pop();
				String left_2 = opStack.pop();
				opStack.push("(" + left_2 + " >> " + right_2 + ")");
				break;
			case iushr:
			case lushr:
				String right_3 = opStack.pop();
				String left_3  = opStack.pop();
				opStack.push("(" + left_3 + " >>> " + right_3 + ")");
				break;
			case iand:
			case land:
				String and =  "(" + opStack.pop() + " & " + opStack.pop() + ")";
				opStack.push(and);
				break;
			case ior:
			case lor:
				String or =  "(" + opStack.pop() + " | " + opStack.pop() + ")";
				opStack.push(or);
				break;
			case ixor:
			case lxor:
				String xor =  "(" + opStack.pop() + " ^ " + opStack.pop() + ")";
				opStack.push(xor);
				break;
			case iinc: break; //**************************************************//
			case i2l:
				String intToLong = "((long)" + opStack.pop() + ")";
				opStack.push(intToLong);
				break;
			case i2f:
				String intToFloat = "((float)" + opStack.pop() + ")";
				opStack.push(intToFloat);
				break;
			case i2d:
				String intToDouble = "((double)" + opStack.pop() + ")";
				opStack.push(intToDouble);
				break;
			case l2i:
				String longToInt = "((int)" + opStack.pop() + ")";
				opStack.push(longToInt);
				break;
			case l2f:
				String longToFloat = "((float)" + opStack.pop() + ")";
				opStack.push(longToFloat);
				break;
			case l2d:
				String longToDouble = "((double)" + opStack.pop() + ")";
				opStack.push(longToDouble);
				break;
			case f2i:
				String floatToInt = "((int)" + opStack.pop() + ")";
				opStack.push(floatToInt);
				break;
			case f2l:
				String floatToLong = "((long)" + opStack.pop() + ")";
				opStack.push(floatToLong);
				break;
			case f2d:
				String floatToDouble = "((double)" + opStack.pop() + ")";
				opStack.push(floatToDouble);
				break;
			case d2i:
				String doubleToInt = "((int)" + opStack.pop() + ")";
				opStack.push(doubleToInt);
				break;
			case d2l:
				String doubleToLong = "((long)" + opStack.pop() + ")";
				opStack.push(doubleToLong);
				break;
			case d2f:
				String doubleToFloat = "((double)" + opStack.pop() + ")";
				opStack.push(doubleToFloat);
				break;
			case i2b:
				String intToByte = "((byte)" + opStack.pop() + ")";
				opStack.push(intToByte);
				break;
			case i2c:
				String intToChar = "((char)" + opStack.pop() + ")";
				opStack.push(intToChar);
				break;
			case i2s:
				String intToShort = "((short)" + opStack.pop() + ")";
				opStack.push(intToShort);
				break;
			case lcmp:
			case fcmpl:
			case fcmpg:
			case dcmpl:
			case dcmpg:
				String cmpNum_2 = opStack.pop();
				String cmpNum_1 = opStack.pop();
				opStack.push("(" + cmpNum_1 + "OPERATOR" + cmpNum_2 + ")");
				break;
			case ifeq:
				String eq = opStack.pop().replace("OPERATOR", " != ");
				break;
			case ifne:
				String ne = opStack.pop().replace("OPERATOR", " == ");
				break;
			case iflt:
				String lt = opStack.pop().replace("OPERATOR", " >= ");
				break;
			case ifge:
				String ge = opStack.pop().replace("OPERATOR", " < ");
				break;
			case ifgt:
				String gt = opStack.pop().replace("OPERATOR", " <= ");
				break;
			case ifle:
				String le = opStack.pop().replace("OPERATOR", " > ");
				break;
			case if_icmpeq:
				String ieq_2 = opStack.pop();
				String ieq_1 = opStack.pop();
				String eqExpr = "(" + ieq_1 + " == " + ieq_2 + ")";
				break;
			case if_icmpne:
				String ine_2 = opStack.pop();
				String ine_1 = opStack.pop();
				String neExpr = "(" + ine_1 + " != " + ine_2 + ")";
				break;
			case if_icmplt:
				String ilt_2 = opStack.pop();
				String ilt_1 = opStack.pop();
				String ltExpr = "(" + ilt_1 + " < " + ilt_2 + ")";
				break;
			case if_icmpge:
				String ige_2 = opStack.pop();
				String ige_1 = opStack.pop();
				String geExpr = "(" + ige_1 + " >= " + ige_2 + ")";
				break;
			case if_icmpgt:
				String igt_2 = opStack.pop();
				String igt_1 = opStack.pop();
				String gtExpr = "(" + igt_1 + " > " + igt_2 + ")";
				break;
			case if_icmple:
				String ile_2 = opStack.pop();
				String ile_1 = opStack.pop();
				String leExpr = "(" + ile_1 + " <= " + ile_2 + ")";
				break;
			case if_acmpeq:
				String aeq_2 = opStack.pop();
				String aeq_1 = opStack.pop();
				String aeqExpr = "(" + aeq_1 + " == " + aeq_2 + ")";
				break;
			case if_acmpne:
				String ane_2 = opStack.pop();
				String ane_1 = opStack.pop();
				String aneExpr = "(" + ane_1 + " != " + ane_2 + ")";
				break;
			case _goto: break;
			case jsr: break;
			case ret: break;
			case tableswitch: break;
			case lookupswitch: break;
			case ireturn:
			case lreturn:
			case freturn:
			case dreturn:
			case areturn:
				String retHasOperand = "return " + opStack.pop();
				break;
			case _return: break;
			case getstatic: break;
			case putstatic: break;
			case getfield: break;
			case putfield: break;
			case invokevirtual: break;
			case invokespecial: break;
			case invokestatic: break;
			case invokeinterface: break;
			case inovokedynamic: break;
			case _new:
				String newClass = ((CpRefOpcode)opcode).getOperand();
				opStack.push(newClass.replace("/", "."));
				break;
			case newarray:
				String type = ((NewArray)opcode).getType();
				String primLen = opStack.pop();
				opStack.push("new " + type + "[" + primLen + "]");
				break;
			case anewarray:
				String objType = ((CpRefOpcode)opcode).getOperand();
				String objLen = opStack.pop();
				opStack.push("new " + objType.replace("/", ".") + "[" + objLen + "]");
				break;
			case arraylength:
				opStack.push(opStack.pop() + ".length");
				break;
			case athrow: break;
			case checkcast:
				String casted = ((CpRefOpcode)opcode).getOperand().replace("/", ".");
				opStack.push("((" + casted + ")" + opStack.pop() + ")");
				break;
			case _instanceof:
				String instanceType = ((CpRefOpcode)opcode).getOperand().replace("/", ".");
				opStack.push("(" + opStack.pop() + " instanceof " + instanceType + ")");
				break;
			case monitorenter: break;
			case monitorexit: break;
			case wide: break;
			case multianewarray:
				MultiANewArray mana = (MultiANewArray)opcode;
				String[] dimArray = new String[mana.getDemensions()];
				for(int i = 0; i < dimArray.length; i++) {
					dimArray[i] = opStack.pop();
				}
				StringBuilder manArray = new StringBuilder();
				manArray.append("new ").append(mana.getOperand().replace("/", "."));
				for(int i = 0; i < dimArray.length - 1; i++) {
					manArray.append("[").append(dimArray[i]).append("]");
				}
				manArray.append("[").append(dimArray[dimArray.length - 1]).append("]");
				opStack.push(manArray.toString());
				break;
			case ifnull: 
				String ifn = "if(" + opStack.pop() + " == null) {";
				break;
			case ifnonnull:
				String ifnonn = "if(" + opStack.pop() + " != null) {";
				break;
			case goto_w: break;
			case jsr_w: break;
			case breakpoint: break;
			case impdep1: break;
			case impdep2: break;
			default:
				break;
		}
	}
}