package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.LineInstructions;
import sds.assemble.MethodContent;
import sds.assemble.controlflow.CFNode;
import sds.classfile.bytecode.CpRefOpcode;
import sds.classfile.bytecode.Iinc;
import sds.classfile.bytecode.IndexOpcode;
import sds.classfile.bytecode.MultiANewArray;
import sds.classfile.bytecode.NewArray;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.PushOpcode;

import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.classfile.bytecode.MnemonicTable.*;
import static sds.util.Printer.println;

/**
 * This class is for decompiling contents of method.
 * @author inagaki
 */
public class MethodDecompiler extends AbstractDecompiler {
	private OperandStack opStack;
	private LocalStack local;
	private String caller;

	/**
	 * constructor.
	 * @param result decompiled source
	 * @param caller caller class has this method
	 */
	public MethodDecompiler(DecompiledResult result, String caller) {
		super(result);
		this.caller = caller;
	}

	@Override
	public void decompile(BaseContent content) {
		MethodContent method = (MethodContent)content;
		this.opStack = new OperandStack();
		this.local = new LocalStack();
		addAnnotation(method.getAnnotation());
		addDeclaration(method);
		result.changeIndent(DecompiledResult.INCREMENT);
		result.writeEndScope();
	}

	@Override
	void addDeclaration(BaseContent content) {
		MethodContent method = (MethodContent)content;
		StringBuilder methodDeclaration = new StringBuilder();
		// access flag
		methodDeclaration.append(method.getAccessFlag());

		// in case of method is not static initializer
		if(! method.getName().equals("<clinit>")) {
			if(method.getName().contains("<init>")) {
				// in case of Constructor, it is unnecessary return type declaration.
				methodDeclaration.append(method.getName().replace("<init>", caller)).append("(");
			} else {
				String desc = method.getDescriptor();
				String returnType = desc.substring(desc.indexOf(")") + 1, desc.length());
				methodDeclaration.append(returnType).append(" ").append(method.getName()).append("(");
			}
			// args
			if(! method.getAccessFlag().contains("static")) {
				// in case of method is not static, the method has own as argument.
				local.push("this");
			}
			String[][] args = method.getArgs();
			int length = args.length;
			if(length > 0) {
				for(int i = 0; i < length - 1 ; i++) {
					methodDeclaration.append(args[i][0]).append(" ").append(args[i][1]).append(", ");
					if(args[i][0].matches("double|long")) {
						local.push("arg_" + i);
						local.push("arg_" + i);
					} else {
						local.push("arg_" + i);
					}
				}
				methodDeclaration.append(args[length - 1][0]).append(" ").append(args[length - 1][1]);
				if(args[args.length - 1][0].matches("double|long")) {
					local.push("arg_" + (length - 1));
					local.push("arg_" + (length - 1));
				} else {
					local.push("arg_" + (length - 1));
				}
			}
			methodDeclaration.append(") ");
		}
		// throws statement
		if(method.getExceptions().length > 0) {
			methodDeclaration.append("throws ");
			String[] exceptions = method.getExceptions();
			for(int i = 0; i < exceptions.length - 1; i++) {
				methodDeclaration.append(exceptions[i]).append(", ");
			}
			methodDeclaration.append(exceptions[exceptions.length - 1]).append(" ");
		}
		// abstract or other method
		if(method.getAccessFlag().contains("abstract")) {
			methodDeclaration.append(";");
		} else {
			methodDeclaration.append("{");
		}
		// method declaration
		// ex). public void method(int i, int k) throws Exception {...
		result.write(methodDeclaration.toString());
		println(">>> " + methodDeclaration.toString());

		// method body
		result.changeIndent(DecompiledResult.INCREMENT);
		buildMethodBody(method.getInst(), method.getNodes());
		result.changeIndent(DecompiledResult.DECREMENT);
	}

	private void buildMethodBody(LineInstructions[] insts, CFNode[] nodes) {
		for(int i = 0; i < insts.length; i++) {
			CFNode node = nodes[i];
			LineInstructions inst = insts[i];
			StringBuilder line = new StringBuilder();
			OpcodeInfo[] opcodes = inst.getOpcodes().getAll();
			for(OpcodeInfo opcode : opcodes) {
				println(opcode + ", current: " + opStack.getCurrentStackSize());
				println("local: " + local);
				println("stack: " + opStack);
				switch(opcode.getOpcodeType()) {
					case nop: break;
					case aconst_null: opStack.push("null"); break;
					case iconst_m1:   opStack.push(-1);     break;
					case iconst_0:    opStack.push(0);      break;
					case iconst_1:    opStack.push(1);      break;
					case iconst_2:    opStack.push(2);      break;
					case iconst_3:    opStack.push(3);      break;
					case iconst_4:    opStack.push(4);      break;
					case iconst_5:    opStack.push(5);      break;
					case lconst_0:    opStack.push(0L);     break;
					case lconst_1:    opStack.push(1L);     break;
					case fconst_0:    opStack.push(0.0f);   break;
					case fconst_1:    opStack.push(1.0f);   break;
					case fconst_2:    opStack.push(2.0f);   break;
					case dconst_0:    opStack.push(0.0d);   break;
					case dconst_1:    opStack.push(1.0d);   break;
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
					case iload_0: opStack.push(local.load(0)); break;
					case iload_1: opStack.push(local.load(1)); break;
					case iload_2: opStack.push(local.load(2)); break;
					case iload_3: opStack.push(local.load(3)); break;
					case lload_0: opStack.push(local.load(0)); break;
					case lload_1: opStack.push(local.load(1)); break;
					case lload_2: opStack.push(local.load(2)); break;
					case lload_3: opStack.push(local.load(3)); break;
					case fload_0: opStack.push(local.load(0)); break;
					case fload_1: opStack.push(local.load(1)); break;
					case fload_2: opStack.push(local.load(2)); break;
					case fload_3: opStack.push(local.load(3)); break;
					case dload_0: opStack.push(local.load(0)); break;
					case dload_1: opStack.push(local.load(1)); break;
					case dload_2: opStack.push(local.load(2)); break;
					case dload_3: opStack.push(local.load(3)); break;
					case aload_0: opStack.push(local.load(0)); break;
					case aload_1: opStack.push(local.load(1)); break;
					case aload_2: opStack.push(local.load(2)); break;
					case aload_3: opStack.push(local.load(3)); break;
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
					case istore:
					case fstore:
					case astore:
						IndexOpcode inOp = (IndexOpcode)opcode;
						line.append(local.load(inOp.getIndex())).append(" = ").append(opStack.pop());
						break;
					case lstore:
					case dstore:
						IndexOpcode inOpDL = (IndexOpcode)opcode;
						line.append(local.load(inOpDL.getIndex(), true)).append(" = ").append(opStack.pop());
						break;
					case istore_0: line.append(local.load(0)).append(" = ").append(opStack.pop()); break;
					case istore_1: line.append(local.load(1)).append(" = ").append(opStack.pop()); break;
					case istore_2: line.append(local.load(2)).append(" = ").append(opStack.pop()); break;
					case istore_3: line.append(local.load(3)).append(" = ").append(opStack.pop()); break;
					case fstore_0: line.append(local.load(0)).append(" = ").append(opStack.pop()); break;
					case fstore_1: line.append(local.load(1)).append(" = ").append(opStack.pop()); break;
					case fstore_2: line.append(local.load(2)).append(" = ").append(opStack.pop()); break;
					case fstore_3: line.append(local.load(3)).append(" = ").append(opStack.pop()); break;
					case astore_0: line.append(local.load(0)).append(" = ").append(opStack.pop()); break;
					case astore_1: line.append(local.load(1)).append(" = ").append(opStack.pop()); break;
					case astore_2: line.append(local.load(2)).append(" = ").append(opStack.pop()); break;
					case astore_3: line.append(local.load(3)).append(" = ").append(opStack.pop()); break;
					case lstore_0: line.append(local.load(0, true)).append(" = ").append(opStack.pop()); break;
					case lstore_1: line.append(local.load(1, true)).append(" = ").append(opStack.pop()); break;
					case lstore_2: line.append(local.load(2, true)).append(" = ").append(opStack.pop()); break;
					case lstore_3: line.append(local.load(3, true)).append(" = ").append(opStack.pop()); break;
					case dstore_0: line.append(local.load(0, true)).append(" = ").append(opStack.pop()); break;
					case dstore_1: line.append(local.load(1, true)).append(" = ").append(opStack.pop()); break;
					case dstore_2: line.append(local.load(2, true)).append(" = ").append(opStack.pop()); break;
					case dstore_3: line.append(local.load(3, true)).append(" = ").append(opStack.pop()); break;
					case iastore:
					case lastore:
					case fastore:
					case dastore:
					case aastore:
					case bastore:
					case castore:
					case sastore:
						String arrayRef = opStack.pop();
						int index = Integer.parseInt(opStack.pop());
						String storingValue = opStack.pop();
						line.append(arrayRef).append("[").append(index).append("]")
							.append(" = ").append(storingValue);
						break;
					case pop:
						line.append(opStack.pop());
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
					case dadd: calculate(" + "); break;
					case isub:
					case lsub:
					case fsub:
					case dsub: calculate(" - "); break;
					case imul:
					case lmul:
					case fmul:
					case dmul: calculate(" * "); break;
					case idiv:
					case ldiv:
					case fdiv:
					case ddiv: calculate(" / "); break;
					case irem:
					case lrem:
					case frem:
					case drem: calculate(" % "); break;
					case ineg:
					case lneg:
					case fneg:
					case dneg:
						String minus = "-(" + opStack.pop() + ")";
						opStack.push(minus);
						break;
					case ishl:
					case lshl:  calculate(" << ");  break;
					case ishr:
					case lshr:  calculate(" >> ");  break;
					case iushr:
					case lushr: calculate(" >>> "); break;
					case iand:
					case land:  calculate(" & ");   break;
					case ior:
					case lor:   calculate(" | ");   break;
					case ixor:
					case lxor:  calculate(" ^ ");   break;
					case iinc:
						Iinc inc = (Iinc)opcode;
						line.append(local.load(inc.getIndex()));
						int _const = inc.getConst();
						if(_const == 1) {
							line.append("++");
						} else if(_const == -1) {
							line.append("--");
						} else if(_const > 1) {
							line.append(" += ").append(_const);
						} else if(_const < -1) {
							line.append(" -= ").append(_const);
						}
						// in case of "_const == 0", ignoring
						break;
					case i2l: castPrimitive("long");   break;
					case i2f: castPrimitive("float");  break;
					case i2d: castPrimitive("double"); break;
					case l2i: castPrimitive("int");    break;
					case l2f: castPrimitive("float");  break;
					case l2d: castPrimitive("double"); break;
					case f2i: castPrimitive("int");    break;
					case f2l: castPrimitive("long");   break;
					case f2d: castPrimitive("double"); break;
					case d2i: castPrimitive("int");    break;
					case d2l: castPrimitive("long");   break;
					case d2f: castPrimitive("float");  break;
					case i2b: castPrimitive("byte");   break;
					case i2c: castPrimitive("char");   break;
					case i2s: castPrimitive("short");  break;
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
						// if(node.getType() == LoopEntry) {
							
						// } else {
						// 	String eq = opStack.pop().replace("OPERATOR", " != ");
						// }
						break;
					case ifne:
						// if(node.getType() == LoopEntry) {
							
						// } else {
						// 	String ne = opStack.pop().replace("OPERATOR", " == ");
						// }
						break;
					case iflt:
						// if(node.getType() == LoopEntry) {
							
						// } else {
						// 	String lt = opStack.pop().replace("OPERATOR", " >= ");
						// }
						break;
					case ifge:
						// if(node.getType() == LoopEntry) {
							
						// } else {
						// 	String ge = opStack.pop().replace("OPERATOR", " < ");
						// }
						break;
					case ifgt:
						// if(node.getType() == LoopEntry) {
							
						// } else {
						// 	String gt = opStack.pop().replace("OPERATOR", " <= ");
						// }
						break;
					case ifle:
						// if(node.getType() == LoopEntry) {
							
						// } else {
						// 	String le = opStack.pop().replace("OPERATOR", " > ");
						// }
						break;
					case if_icmpeq:
						String ieq_2 = opStack.pop();
						String ieq_1 = opStack.pop();
						line.append("if(").append(ieq_1).append(" == ").append(ieq_2).append(")");
						break;
					case if_icmpne:
						String ine_2 = opStack.pop();
						String ine_1 = opStack.pop();
						line.append("if(").append(ine_1).append(" != ").append(ine_2).append(")");
						break;
					case if_icmplt:
						String ilt_2 = opStack.pop();
						String ilt_1 = opStack.pop();
						line.append("if(").append(ilt_1).append(" < ").append(ilt_2).append(")");
						break;
					case if_icmpge:
						String ige_2 = opStack.pop();
						String ige_1 = opStack.pop();
						line.append("if(").append(ige_1).append(" >= ").append(ige_2).append(")");
						break;
					case if_icmpgt:
						String igt_2 = opStack.pop();
						String igt_1 = opStack.pop();
						line.append("if(").append(igt_1).append(" > ").append(igt_2).append(")");
						break;
					case if_icmple:
						String ile_2 = opStack.pop();
						String ile_1 = opStack.pop();
						line.append("if(").append(ile_1).append(" <= ").append(ile_2).append(")");
						break;
					case if_acmpeq:
						String aeq_2 = opStack.pop();
						String aeq_1 = opStack.pop();
						line.append("if(").append(aeq_1).append(" == ").append(aeq_2).append(")");
						break;
					case if_acmpne:
						String ane_2 = opStack.pop();
						String ane_1 = opStack.pop();
						line.append("if(").append(ane_1).append(" != ").append(ane_2).append(")");
						break;
					case _goto:
						break;
					case jsr: break;
					case ret: break;
					case tableswitch: break;
					case lookupswitch: break;
					case ireturn:
					case lreturn:
					case freturn:
					case dreturn:
					case areturn:
						line.append("return ").append(opStack.pop());
						break;
					case _return:
						if(i != nodes.length - 1) {
							// in case of this return instruction is not end of opcode,
							// specifies "return;".
							line.append("return");
						}
						break;
					case getstatic:
						CpRefOpcode getSta = (CpRefOpcode)opcode;
						String getStaticField = getSta.getOperand().split("\\|")[0];
						opStack.push(getStaticField);
						break;
					case putstatic:
						CpRefOpcode putSta = (CpRefOpcode)opcode;
						String putStaticField = putSta.getOperand().split("\\|")[0];
						line.append(putStaticField).append(" = ").append(opStack.pop());
						break;
					case getfield:
						CpRefOpcode getField = (CpRefOpcode)opcode;
						String get = getField.getOperand().split("\\|")[0];
						String getDeclaration = opStack.pop() + ".";
						String[] getNames = get.split("\\.");
						opStack.push(getDeclaration + getNames[getNames.length - 1]);
						break;
					case putfield:
						CpRefOpcode putField = (CpRefOpcode)opcode;
						String put = putField.getOperand().split("\\|")[0];
						String[] putNames = put.split("\\.");
						String value = opStack.pop();
						String putCaller = opStack.pop();
						line.append(putCaller).append(".").append(putNames[putNames.length - 1])
							.append(" = ").append(value);
						break;
					case invokevirtual:
						CpRefOpcode virOpcode = (CpRefOpcode)opcode;
						// 0: xxx.yyy.zzz.method
						// 1: (args_1,args_2,...)returnType
						String[] virOperand = virOpcode.getOperand().split("\\|");
						String virDesc = virOperand[1];
						StringBuilder virtual = new StringBuilder();
						String virArgs = getMethodArgs(virDesc, false);
						// xxx.yyy.zzz.method
						String[] virMethod = virOperand[0].split("\\.");
						// caller.method
						virtual.append(opStack.pop()).append(".").append(virMethod[virMethod.length - 1])
						// caller.method(args1,args2,...)
								.append("(").append(virArgs).append(")");
						if(! pushOntoStack(opcodes, opcode, virtual.toString())) {
							line.append(virtual.toString());
						}
						break;
					case invokespecial:
						CpRefOpcode specialOpcode = (CpRefOpcode)opcode;
						String[] specialOperand = specialOpcode.getOperand().split("\\|");
						String spMethod = specialOperand[0].replace(".<init>", "");
						String spDesc = specialOperand[1];
						String special = "new " + spMethod + "(" + getMethodArgs(spDesc, true) + ")";
						if(! pushOntoStack(opcodes, opcode, special)) {
							line.append(special);
						}
						break;
					case invokestatic:
						CpRefOpcode staticOpcode =(CpRefOpcode)opcode;
						String[] staticOperand = staticOpcode.getOperand().split("\\|");
						String staticDesc = staticOperand[1];
						String st = staticOperand[0] + "(" + getMethodArgs(staticDesc, false) + ")";
						if(! pushOntoStack(opcodes, opcode, st)) {
							line.append(st);
						}
						break;
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
						for(int j = 0; j < dimArray.length; j++) {
							dimArray[j] = opStack.pop();
						}
						StringBuilder manArray = new StringBuilder();
						manArray.append("new ").append(mana.getOperand().replace("/", "."));
						for(int j = 0; j < dimArray.length - 1; j++) {
							manArray.append("[").append(dimArray[j]).append("]");
						}
						manArray.append("[").append(dimArray[dimArray.length - 1]).append("]");
						opStack.push(manArray.toString());
						break;
					case ifnull:
						if(node.getType() == LoopEntry) {
							
						} else {
							String ifn = "if(" + opStack.pop() + " == null) {";
						}
						break;
					case ifnonnull:
						if(node.getType() == LoopEntry) {
							
						} else {
							String ifnonn = "if(" + opStack.pop() + " != null) {";
						}
						break;
					case goto_w: break;
					case jsr_w: break;
					case breakpoint: break;
					case impdep1: break;
					case impdep2: break;
					default: break;
				}
				println(opcode + ", current: " + opStack.getCurrentStackSize());
				println("local: " + local);
				println("stack: " + opStack + "\n");
			}
			// When the node is not start or end of a statement, (ex. "if(...) {", "}")
			// add semicolon and write in the line context.
			if(true) {
				line.append(";");
				result.write(line.toString());
			}
		}
	}

	private void calculate(String operator) {
		String value_1 = opStack.pop(); // value_1
		String value_2  = opStack.pop(); // value_2
		String expr;
		if(operator.contains("<") || operator.contains(">")) {
			expr = "(" + value_2 + operator + value_1 + ")";
		} else {
			expr = "(" + value_1 + operator + value_2 + ")";
		}
		opStack.push(expr);
	}

	private void castPrimitive(String type) {
		String casted = "((" + type + ")" + opStack.pop() + ")";
		opStack.push(casted);
	}

	private String getMethodArgs(String descriptor, boolean isSpecial) {
		StringBuilder sb = new StringBuilder("");
		if((descriptor.indexOf("(") + 1) < descriptor.indexOf(")")) {
			String[] args;
			if(descriptor.contains(",")){
				args = new String[descriptor.split(",").length];
			} else {
				args = new String[1];
			}
			// get arguments from stack
			for(int j = 0; j < args.length; j++) {
				args[j] = opStack.pop();
			}
			println(args);
			// build arguments
			// argN-1, argN-2, ..., arg1
			if(isSpecial) {
				// in case of invokespecial,
				// top element of operand stack is unnecessary to invoke method
				// because the element is pushed by new opcode.
				for(int j = args.length - 1; j > 0; j--) {
					sb.append(args[j]).append(", ");
				}
			} else {
				for(int j = args.length - 1; j > 0; j--) {
					sb.append(args[j]).append(", ");
				}
			}
			// argN-1, argN-2, ..., arg1, arg0
			sb.append(args[0]);
		}
		println("@@@ " + sb.toString());
		return sb.toString();
	}

	/**
	 * this method is for invoke method instruction<br>
	 * 
	 * push element onto operand stack
	 * when specified opcode is end in current node has all opcodes.<br>
	 * 
	 * in case of the opcode is end,
	 * it is necessary to push element onto openrand stack
	 * because there is some processing in the next.<br>
	 * 
	 * on the other hand, in case of that is not end,
	 * it is necessary to write processing of invoking method to decompiled source code
	 * because no opcode is in the next.
	 * 
	 * @param opcodes node has all opcodes
	 * @param opcode one of the node has opcodes.
	 * @param element element for pushing onto operand stack
	 * @return whether pushes element onto operand stack.
	 */
	private boolean pushOntoStack(OpcodeInfo[] opcodes, OpcodeInfo opcode, String element) {
		for(int j = opcodes.length - 1; j >= 0; j--) {
			if(opcodes[j].getPc() == opcode.getPc()) {
				if(j == opcodes.length - 1) {
					// end opcode
					return false;
				}
				opStack.push(element);
				return true;
			}
		}
		throw new IllegalArgumentException("the specified opcode is not in the opcodes.");
	}
}