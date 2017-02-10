package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.LineInstructions;
import sds.assemble.MethodContent;
import sds.assemble.controlflow.CFNode;
import sds.assemble.controlflow.CFEdge;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.CpRefOpcode;
import sds.classfile.bytecode.Iinc;
import sds.classfile.bytecode.IndexOpcode;
import sds.classfile.bytecode.InvokeInterface;
import sds.classfile.bytecode.MultiANewArray;
import sds.classfile.bytecode.MnemonicTable;
import sds.classfile.bytecode.NewArray;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.PushOpcode;

import static sds.assemble.controlflow.NodeTypeChecker.check;
import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.CFNodeType.LoopExit;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntryBreak;
import static sds.assemble.controlflow.CFNodeType.Exit;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;
import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.classfile.bytecode.MnemonicTable.*;
import static sds.decompile.DecompiledResult.INCREMENT;
import static sds.util.Printer.print;
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
				local.push("this", caller);
			}
			String[][] args = method.getArgs();
			int length = args.length;
			if(length > 0) {
				for(int i = 0; i < length - 1 ; i++) {
					methodDeclaration.append(args[i][0]).append(" ").append(args[i][1]).append(", ");
					String type = args[i][0];
					if(type.matches("double|long")) {
						local.push("arg_" + i, type);
						local.push("arg_" + i, type);
					} else {
						local.push("arg_" + i, type);
					}
				}
				String type = args[length - 1][0];
				methodDeclaration.append(type).append(" ").append(args[length - 1][1]);
				if(type.matches("double|long")) {
					local.push("arg_" + (length - 1), type);
					local.push("arg_" + (length - 1), type);
				} else {
					local.push("arg_" + (length - 1), type);
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
//		println(">>> " + methodDeclaration.toString());

		// method body
		result.changeIndent(INCREMENT);
		buildMethodBody(method.getInst(), method.getNodes());
		result.writeEndScope();
	}

	private void buildMethodBody(LineInstructions[] insts, CFNode[] nodes) {
		boolean typePop = true;
		CFNode falseNode = null;
		for(int i = 0; i < insts.length; i++) {
			CFNode node = nodes[i];
			LineInstructions inst = insts[i];
			StringBuilder line = new StringBuilder();
			OpcodeInfo[] opcodes = node.getOpcodes().getAll();
			boolean addSemicolon = true;
			if(check(node, Entry, OneLineEntry, OneLineEntryBreak)) {
				line.append("if(");
				// in the range of
				// Entry-node ~ FALSE-node of the node (jump point node of Entry-node in case of FALSE),
				// there is case that Exit-node doesn't exist.
				// then, it doesn't unindent and add end scope to source code.
				// so, in the case, it holds FALSE-node,
				// and unindents and adds end scope at the time of processing FALSE-node.
				for(CFEdge edge : node.getChildren()) {
					if(edge.getType() == FalseBranch) {
						int index = i;
						CFNode terminal = edge.getDest();
						boolean hasExit = false;
						while(! nodes[index].equals(terminal)) {
							hasExit |= (nodes[index].getType() == Exit);
							index++;
						}
						if(! hasExit) {
							falseNode = terminal;
						}
						break;
					}
				}
			}
			if(falseNode != null && falseNode.equals(node)) {
				result.writeEndScope();
			}
			for(OpcodeInfo opcode : opcodes) {
//				println(opcode + ", current: " + opStack.getCurrentStackSize());
//				println("local: " + local);
				println("stack: " + opStack);
				switch(opcode.getOpcodeType()) {
					case nop: break;
					case aconst_null: opStack.push("null", "null"); break;
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
						CpRefOpcode lcdOpcode = (CpRefOpcode)opcode;
						opStack.push(lcdOpcode.getOperand(), lcdOpcode.getType());
						break;
					case iload:
					case lload:
					case fload:
					case dload:
					case aload:
						load(((IndexOpcode)opcode).getIndex()); break;
					case iload_0:
					case lload_0:
					case fload_0:
					case dload_0:
					case aload_0:
						load(0); break;
					case iload_1:
					case lload_1:
					case fload_1:
					case dload_1:
					case aload_1:
						load(1); break;
					case iload_2:
					case lload_2:
					case fload_2:
					case dload_2:
					case aload_2:
						load(2); break;
					case iload_3:
					case lload_3:
					case fload_3:
					case dload_3:
					case aload_3:
						load(3); break;
					case iaload:
					case laload:
					case faload:
					case daload:
					case aaload:
					case baload:
					case caload:
					case saload:
						String arrayIndex = opStack.pop(typePop);
						String refedArray = opStack.pop();
						String arrayType  = opStack.popType();
						opStack.push(refedArray + "[" + arrayIndex + "]", arrayType);
						break;
					case istore:
					case fstore:
					case astore:
					case lstore:
					case dstore:
						IndexOpcode inOp = (IndexOpcode)opcode;
						line.append(getStored(inOp.getIndex()));
						break;
					case istore_0:
					case fstore_0:
					case astore_0:
					case lstore_0:
					case dstore_0:
						line.append(getStored(0)); break;
					case istore_1:
					case fstore_1:
					case astore_1:
					case lstore_1:
					case dstore_1:
						line.append(getStored(1)); break;
					case istore_2:
					case fstore_2:
					case astore_2:
					case lstore_2:
					case dstore_2:
						line.append(getStored(2)); break;
					case istore_3:
					case fstore_3:
					case astore_3:
					case lstore_3:
					case dstore_3:
						line.append(getStored(3)); break;
					case iastore:
					case lastore:
					case fastore:
					case dastore:
					case aastore:
					case bastore:
					case castore:
					case sastore:
						String storingValue = opStack.pop(typePop);
						int index = Integer.parseInt(opStack.pop(typePop));
						String arrayRef = opStack.pop(typePop);
						line.append(arrayRef).append("[").append(index).append("]")
							.append(" = ").append(storingValue);
						break;
					case pop:
						line.append(opStack.pop(typePop));
						break;
					case pop2:
						opStack.pop(typePop);
						opStack.pop(typePop);
						break;
					case dup:
						String dup = opStack.pop();
						String dupType = opStack.popType();
						opStack.push(dup, dupType);
						opStack.push(dup, dupType);
						break;
					case dup_x1:
						String dup_x1_2  = opStack.pop();
						String dup_x1_1  = opStack.pop();
						String type_x1_2 = opStack.popType();
						String type_x1_1 = opStack.popType();
						opStack.push(dup_x1_1, type_x1_1);
						opStack.push(dup_x1_2, type_x1_2);
						opStack.push(dup_x1_1, type_x1_1);
						break;
					case dup_x2:
						String dup_x2_3  = opStack.pop();
						String dup_x2_2  = opStack.pop();
						String dup_x2_1  = opStack.pop();
						String type_x2_3 = opStack.popType();
						String type_x2_2 = opStack.popType();
						String type_x2_1 = opStack.popType();
						opStack.push(dup_x2_1, type_x2_1);
						opStack.push(dup_x2_2, type_x2_2);
						opStack.push(dup_x2_3, type_x2_3);
						opStack.push(dup_x2_1, type_x2_1);
						break;
					case dup2:
						String dup2_1  = opStack.pop();
						String dup2_2  = opStack.pop();
						String type2_1 = opStack.popType();
						String type2_2 = opStack.popType();
						opStack.push(dup2_2, type2_2);
						opStack.push(dup2_1, type2_1);
						opStack.push(dup2_2, type2_2);
						opStack.push(dup2_1, type2_1);
						break;
					case dup2_x1:
						String dup2_x1_3 = opStack.pop();
						String dup2_x1_2 = opStack.pop();
						String dup2_x1_1 = opStack.pop();
						String type2_x1_3 = opStack.popType();
						String type2_x1_2 = opStack.popType();
						String type2_x1_1 = opStack.popType();
						opStack.push(dup2_x1_1, type2_x1_1);
						opStack.push(dup2_x1_2, type2_x1_2);
						opStack.push(dup2_x1_3, type2_x1_3);
						opStack.push(dup2_x1_1, type2_x1_1);
						opStack.push(dup2_x1_2, type2_x1_2);
						break;
					case dup2_x2:
						String dup2_x2_4 = opStack.pop();
						String dup2_x2_3 = opStack.pop();
						String dup2_x2_2 = opStack.pop();
						String dup2_x2_1 = opStack.pop();
						String type2_x2_4 = opStack.popType();
						String type2_x2_3 = opStack.popType();
						String type2_x2_2 = opStack.popType();
						String type2_x2_1 = opStack.popType();
						opStack.push(dup2_x2_1, type2_x2_1);
						opStack.push(dup2_x2_2, type2_x2_2);
						opStack.push(dup2_x2_3, type2_x2_3);
						opStack.push(dup2_x2_4, type2_x2_4);
						opStack.push(dup2_x2_1, type2_x2_1);
						opStack.push(dup2_x2_2, type2_x2_2);
						break;
					case swap:
						String two = opStack.pop();
						String one = opStack.pop();
						String typeTwo = opStack.popType();
						String typeOne = opStack.popType();
						opStack.push(two, typeTwo);
						opStack.push(one, typeOne);
						break;
					case iadd: calculate(" + ", "int");    break;
					case ladd: calculate(" + ", "long");   break;
					case fadd: calculate(" + ", "float");  break;
					case dadd: calculate(" + ", "double"); break;
					case isub: calculate(" - ", "int");    break;
					case lsub: calculate(" - ", "long");   break;
					case fsub: calculate(" - ", "float");  break;
					case dsub: calculate(" - ", "double"); break;
					case imul: calculate(" * ", "int");    break;
					case lmul: calculate(" * ", "long");   break;
					case fmul: calculate(" * ", "float");  break;
					case dmul: calculate(" * ", "double"); break;
					case idiv: calculate(" / ", "int");    break;
					case ldiv: calculate(" / ", "long");   break;
					case fdiv: calculate(" / ", "float");  break;
					case ddiv: calculate(" / ", "double"); break;
					case irem: calculate(" % ", "int");    break;
					case lrem: calculate(" % ", "long");   break;
					case frem: calculate(" % ", "float");  break;
					case drem: calculate(" % ", "double"); break;
					case ineg:
					case lneg:
					case fneg:
					case dneg:
						String minus = "-(" + opStack.pop() + ")";
						opStack.push(minus, opStack.popType());
						break;
					case ishl:  calculate(" << ", "int");   break;
					case lshl:  calculate(" << ", "long");  break;
					case ishr:  calculate(" >> ", "int");   break;
					case lshr:  calculate(" >> ", "long");  break;
					case iushr: calculate(" >>> ", "int");  break;
					case lushr: calculate(" >>> ", "long"); break;
					case iand:  calculate(" & ", "int");    break;
					case land:  calculate(" & ", "long");   break;
					case ior:   calculate(" | ", "int");    break;
					case lor:   calculate(" | ", "long");   break;
					case ixor:  calculate(" ^ ", "int");    break;
					case lxor:  calculate(" ^ ", "long");   break;
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
						String cmpNum_2 = opStack.pop(typePop);
						String cmpNum_1 = opStack.pop(typePop);
						opStack.push("(" + cmpNum_1 + "OPERATOR" + cmpNum_2 + ")", "boolean");
						break;
					case ifeq:
						if(check(node, LoopEntry)) {
						} else if(check(node, OneLineEntry, OneLineEntryBreak)) {
							line.append(getIfExpr(" == ")).append(") ");
							break;
						} else {
							line.append(getIfExpr(" == "));
						}
						addSemicolon = false;
						break;
					case ifne:
						if(check(node, LoopEntry)) {
						} else if(check(node, OneLineEntry, OneLineEntryBreak)) {
							line.append(getIfExpr(" != ")).append(") ");
							break;
						} else {
							line.append(getIfExpr(" != "));
						}
						addSemicolon = false;
						break;
					case iflt:
						if(check(node, LoopEntry)) {
						} else if(check(node, OneLineEntry, OneLineEntryBreak)) {
							line.append(getIfExpr(" >= ")).append(") ");
							break;
						} else {
							line.append(getIfExpr(" >= "));
						}
						addSemicolon = false;
						break;
					case ifge:
						if(check(node, LoopEntry)) {
						} else if(check(node, OneLineEntry, OneLineEntryBreak)) {
							line.append(getIfExpr(" < ")).append(") ");
							break;
						} else {
							line.append(getIfExpr(" < "));
						}
						addSemicolon = false;
						break;
					case ifgt:
						if(check(node, LoopEntry)) {
						} else if(check(node, OneLineEntry, OneLineEntryBreak)) {
							line.append(getIfExpr(" <= ")).append(") ");
							break;
						} else {
							line.append(getIfExpr(" <= "));
						}
						addSemicolon = false;
						break;
					case ifle:
						if(check(node, LoopEntry)) {
						} else if(check(node, OneLineEntry, OneLineEntryBreak)) {
							line.append(getIfExpr(" > ")).append(") ");
							break;
						} else {
							line.append(getIfExpr(" > "));
						}
						addSemicolon = false;
						break;
					case if_icmpeq:
						String ieq_2 = opStack.pop(typePop);
						String ieq_1 = opStack.pop(typePop);
						line.append("(").append(ieq_1).append(" == ").append(ieq_2).append(")");
						addSemicolon = false;
						break;
					case if_icmpne:
						String ine_2 = opStack.pop(typePop);
						String ine_1 = opStack.pop(typePop);
						line.append("(").append(ine_1).append(" != ").append(ine_2).append(")");
						addSemicolon = false;
						break;
					case if_icmplt:
						String ilt_2 = opStack.pop(typePop);
						String ilt_1 = opStack.pop(typePop);
						line.append("(").append(ilt_1).append(" < ").append(ilt_2).append(")");
						addSemicolon = false;
						break;
					case if_icmpge:
						String ige_2 = opStack.pop(typePop);
						String ige_1 = opStack.pop(typePop);
						line.append("(").append(ige_1).append(" >= ").append(ige_2).append(")");
						addSemicolon = false;
						break;
					case if_icmpgt:
						String igt_2 = opStack.pop(typePop);
						String igt_1 = opStack.pop(typePop);
						line.append("(").append(igt_1).append(" > ").append(igt_2).append(")");
						addSemicolon = false;
						break;
					case if_icmple:
						String ile_2 = opStack.pop(typePop);
						String ile_1 = opStack.pop(typePop);
						line.append("(").append(ile_1).append(" <= ").append(ile_2).append(")");
						addSemicolon = false;
						break;
					case if_acmpeq:
						String aeq_2 = opStack.pop(typePop);
						String aeq_1 = opStack.pop(typePop);
						line.append("(").append(aeq_1).append(" == ").append(aeq_2).append(")");
						addSemicolon = false;
						break;
					case if_acmpne:
						String ane_2 = opStack.pop(typePop);
						String ane_1 = opStack.pop(typePop);
						line.append("(").append(ane_1).append(" != ").append(ane_2).append(")");
						addSemicolon = false;
						break;
					case _goto:
//						addSemicolon = false;
//						result.write(line.toString());
//						line = line.delete(0, line.length());
						if(line.length() == 0) {
							line.append(opStack.pop(typePop));
						}
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
						line.append("return ").append(opStack.pop(typePop));
						break;
					case _return:
						if(i != nodes.length - 1) {
							// in case of this return instruction is not end of opcode,
							// specifies "return;".
							line.append("return;");
						}
						break;
					case getstatic:
						CpRefOpcode getSta = (CpRefOpcode)opcode;
						// Xxx.yyy.FIELD|type
						String[] getStaticField = getSta.getOperand().split("\\|");
						opStack.push(getStaticField[0], getStaticField[1]);
						break;
					case putstatic:
						CpRefOpcode putSta = (CpRefOpcode)opcode;
						String putStaticField = putSta.getOperand().split("\\|")[0];
						line.append(putStaticField).append(" = ").append(opStack.pop(typePop));
						break;
					case getfield:
						CpRefOpcode getField = (CpRefOpcode)opcode;
						String get[] = getField.getOperand().split("\\|");
						String getDeclaration = opStack.pop(typePop) + ".";
						String[] getNames = get[0].split("\\.");
						opStack.push(getDeclaration + getNames[getNames.length - 1], get[1]);
						break;
					case putfield:
						CpRefOpcode putField = (CpRefOpcode)opcode;
						String put = putField.getOperand().split("\\|")[0];
						String[] putNames = put.split("\\.");
						String value = opStack.pop(typePop);
						String putCaller = opStack.pop(typePop);
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
						String virArgs = getMethodArgs(virDesc);
						// xxx.yyy.zzz.method
						String[] virMethod = virOperand[0].split("\\.");
						// caller.method
						virtual.append(opStack.pop(typePop)).append(".").append(virMethod[virMethod.length - 1])
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
						// stack: [obj, obj, arg_1, ..., arg_N]
						// stack: [obj, obj] (after calling getMethodArgs())
						String special = "new " + spMethod + "(" + getMethodArgs(spDesc) + ")";
						// stack: [obj]
						opStack.pop(typePop);
						if(! pushOntoStack(opcodes, opcode, special)) {
							line.append(special);
						} else {
							// stack: [obj, invoked_method]
							// stack: [obj]
							String element = opStack.pop();
							String type    = opStack.popType();
							// stack: []
							opStack.pop(typePop);
							// stack: [invoked_method]
							opStack.push(element, type);
						}
						break;
					case invokestatic:
						CpRefOpcode staticOpcode =(CpRefOpcode)opcode;
						String[] staticOperand = staticOpcode.getOperand().split("\\|");
						String staticDesc = staticOperand[1];
						String st = staticOperand[0] + "(" + getMethodArgs(staticDesc) + ")";
						if(! pushOntoStack(opcodes, opcode, st)) {
							line.append(st);
						}
						break;
					case invokeinterface:
						InvokeInterface interfaceOpcode = (InvokeInterface)opcode;
						String[] interOperand = interfaceOpcode.getOperand().split("\\|");
						String interDesc = interOperand[1];
						String[] interMethod = interOperand[0].split("\\.");
						String interArgs = getMethodArgs(interDesc);
						StringBuilder inter = new StringBuilder();
						inter.append(opStack.pop(typePop)).append(".").append(interMethod[interMethod.length - 1])
							.append("(").append(interArgs).append(")");
						if(! pushOntoStack(opcodes, opcode, inter.toString())) {
							line.append(inter.toString());
						}
						break;
					case inovokedynamic: break;
					case _new:
						String newClass = ((CpRefOpcode)opcode).getOperand();
						String classType = newClass.replace("/", ".");
						opStack.push(classType, classType);
						break;
					case newarray:
						String type = ((NewArray)opcode).getType();
						String primLen = opStack.pop(typePop);
						opStack.push("new " + type + "[" + primLen + "]", type + "[]");
						break;
					case anewarray:
						String objType = ((CpRefOpcode)opcode).getOperand();
						String aNewArrayType = objType.replace("/", ".");
						String objLen = opStack.pop(typePop);
						opStack.push("new " + aNewArrayType + "[" + objLen + "]", aNewArrayType + "[]");
						break;
					case arraylength:
						opStack.push(opStack.pop(typePop) + ".length", "int");
						break;
					case athrow: break;
					case checkcast:
						String casted = ((CpRefOpcode)opcode).getOperand().replace("/", ".");
						opStack.push("((" + casted + ")" + opStack.pop(typePop) + ")", casted);
						break;
					case _instanceof:
						String instanceType = ((CpRefOpcode)opcode).getOperand().replace("/", ".");
						opStack.push("(" + opStack.pop(typePop) + " instanceof " + instanceType + ")", "boolean");
						break;
					case monitorenter: break;
					case monitorexit: break;
					case wide: break;
					case multianewarray:
						MultiANewArray mana = (MultiANewArray)opcode;
						String multiArrayType = mana.getOperand().replace("/", ".");
						String[] dimArray = new String[mana.getDimensions()];
						for(int j = 0; j < dimArray.length; j++) {
							dimArray[j] = opStack.pop(typePop);
						}
						StringBuilder manArray = new StringBuilder("new ");
						// new XXX
						manArray.append(multiArrayType.substring(0, multiArrayType.indexOf("]") - 1));
						for(int j = dimArray.length - 1; j > 0; j--) {
							manArray.append("[").append(dimArray[j]).append("]");
						}
						// new XXX[n][m]...
						manArray.append("[").append(dimArray[0]).append("]");
						opStack.push(manArray.toString(), multiArrayType);
						break;
					case ifnull:
						if(node.getType() == LoopEntry) {
							
						} else {
							line.append("(").append(opStack.pop(typePop)).append(" == null)");
						}
						addSemicolon = false;
						break;
					case ifnonnull:
						if(node.getType() == LoopEntry) {
							
						} else {
							line.append("(").append(opStack.pop(typePop)).append(" != null)");
						}
						addSemicolon = false;
						break;
					case goto_w: break;
					case jsr_w: break;
					case breakpoint: break;
					case impdep1: break;
					case impdep2: break;
					default: break;
				}
//				println("local: " + local);
				println("stack: " + opStack + "\n");
			}
			if(check(node, LoopEntry, Entry)) {
				line.append(") {");
			}
			if(line.length() > 0) {
				// When the node is not start or end of a statement, (ex. "if(...) {", "}")
				// add semicolon and write in the line context.
				if(addSemicolon) {
					line.append(";");
				}
				result.write(line.toString());
			}
			indent(node);
		}
	}

	private void indent(CFNode node) {
		if(check(node, LoopEntry, Entry)) {
			result.changeIndent(INCREMENT);
		}
		if(check(node, LoopExit, Exit)) {
			result.writeEndScope();
		}
	}

	private void calculate(String operator, String type) {
		String value_1 = opStack.pop(true); // right
		String value_2 = opStack.pop(true); // left
		String expr;
		if(operator.contains("<") || operator.contains(">")) {
			// shift operator
			expr = "(" + value_2 + operator + value_1 + ")";
		} else {
			expr = "(" + value_1 + operator + value_2 + ")";
		}
		opStack.push(expr, type);
	}

	private void load(int index) {
		opStack.push(local.load(index), local.loadType(index));
	}

	private void castPrimitive(String type) {
		String casted = "((" + type + ")" + opStack.pop(true) + ")";
		opStack.push(casted, type);
	}

	private String addLogicOperator(CFNode node, String line) {
		if(! line.contains("#LOGIC#")) {
			return line;
		}
		int index = 0;
		BranchOpcode[] branches = new BranchOpcode[node.getJumpPoints().length];
		for(OpcodeInfo opcode : node.getOpcodes().getAll()) {
			if((opcode instanceof BranchOpcode)) {
				BranchOpcode branch = (BranchOpcode)opcode;
				if(branch.isIf()) {
					branches[index] = branch;
					index++;
				}
			}
		}
		String[] exprs  = line.split("#LOGIC#");
		String[] logics = new String[exprs.length - 1];
		StringBuilder ifLine = new StringBuilder();
		for(int i = 0; i < branches.length; i++) {
			if(i < branches.length - 1) {
				int jump = branches[i].getBranch();
				for(CFEdge edge : node.getChildren()) {
					if((edge.getType() == TrueBranch) && edge.getDest().isInPcRange(jump)) {
						ifLine.append(changeToNegative(branches[i].getOpcodeType(), exprs[i]));
					} else if((edge.getType() == FalseBranch) && edge.getDest().isInPcRange(jump)) {
						logics[i] = "&&";
					}
				}
			} else {
				ifLine.append(exprs[i]);
			}
		}
		return ifLine.toString();
	}

	private String changeToNegative(MnemonicTable type, String condition) {
		switch(type) {
			case if_icmpeq:
			case if_acmpeq:
			case ifnull:
			case ifeq:
				if(condition.contains("==")) {
					return condition.replace("==", "!=");
				}
				return "(! " + condition + ")";
			case if_icmpne:
			case if_acmpne:
			case ifnonnull:
			case ifne:      return condition.replace("!=", "==");
			case if_icmplt:
			case iflt:      return condition.replace("<" , ">=");
			case if_icmpgt:
			case ifgt:      return condition.replace(">" , "<=");
			case if_icmple:
			case ifle:      return condition.replace("<=", ">");
			case if_icmpge:
			case ifge:      return condition.replace(">=", "<");
			default:
				throw new IllegalArgumentException(type + " opcode is not if_xx opcode.");
		}
	}

	private String getIfExpr(String cmpOperator) {
		String expr = opStack.pop();
		String type = opStack.popType();
		if(expr.contains("OPERATOR") || type.equals("boolean")) {
			return expr.replace("OPERATOR", cmpOperator);
		}
		// in case of comparing int value and 0, no opcode push 0 onto stack.
		// so, it is necessary to append comparing operator and 0 to popped element.
		return "(" + expr + cmpOperator + "0)";
	}

	private String getStored(int index) {
		String stored = opStack.pop();
		String type  = opStack.popType();
		int before = local.size();
		String value = local.load(index, type);
		int after  = local.size();
		if(before == after) {
			// storing
			return value + " = " + stored;
		}
		// declaring
		return type + " " + value + " = " + stored;
	}

	private String getMethodArgs(String descriptor) {
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
				args[j] = opStack.pop(true);
			}
			// build arguments
			// argN-1, argN-2, ..., arg1
			for(int j = args.length - 1; j > 0; j--) {
				sb.append(args[j]).append(", ");
			}
			// argN-1, argN-2, ..., arg1, arg0
			sb.append(args[0]);
		}
		return sb.toString();
	}

	/**
	 * this method is for invoke method instruction.
	 * 
	 * push element onto operand stack
	 * when specified opcode is end in current node has all opcodes.
	 * 
	 * in case of the opcode is end,
	 * it is necessary to push element onto openrand stack
	 * because there is some processing in the next.
	 * 
	 * on the other hand, in case of that is not end,
	 * it is necessary to write processing of invoking method on decompiled source
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
				CpRefOpcode invoke = (CpRefOpcode)opcode;
				String[] operand = invoke.getOperand().split("\\|");
				String desc = operand[1];
				String type = operand[0].endsWith("<init>") ?
						operand[0].replace(".<init>", "") : desc.substring(desc.indexOf(")") + 1);
				opStack.push(element, type);
				return true;
			}
		}
		throw new IllegalArgumentException("the specified opcode is not in the opcodes.");
	}
}