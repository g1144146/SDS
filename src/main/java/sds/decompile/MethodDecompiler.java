package sds.decompile;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.StringJoiner;
import sds.decompile.stack.LocalStack;
import sds.decompile.stack.OperandStack;
import sds.assemble.BaseContent;
import sds.assemble.MethodContent;
import sds.assemble.controlflow.CFNode;
import sds.assemble.controlflow.CFEdge;
import sds.classfile.bytecode.CpRefOpcode;
import sds.classfile.bytecode.Iinc;
import sds.classfile.bytecode.IndexOpcode;
import sds.classfile.bytecode.InvokeInterface;
import sds.classfile.bytecode.MultiANewArray;
import sds.classfile.bytecode.NewArray;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.PushOpcode;
import sds.decompile.cond_expr.ConditionalExprBuilder;

import static sds.assemble.controlflow.CFNodeType.LoopEntry;
import static sds.assemble.controlflow.CFNodeType.LoopExit;
import static sds.assemble.controlflow.CFNodeType.Entry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntry;
import static sds.assemble.controlflow.CFNodeType.OneLineEntryBreak;
import static sds.assemble.controlflow.CFNodeType.Exit;
import static sds.assemble.controlflow.CFNodeType.SynchronizedEntry;
import static sds.assemble.controlflow.CFNodeType.SynchronizedExit;
import static sds.assemble.controlflow.CFEdgeType.TrueBranch;
import static sds.assemble.controlflow.CFEdgeType.FalseBranch;
import static sds.classfile.bytecode.MnemonicTable.*;
import static sds.decompile.DecompiledResult.INCREMENT;

import static sds.assemble.controlflow.NodeTypeChecker.check;
import static sds.assemble.controlflow.NodeTypeChecker.checkNone;
import static sds.util.Printer.println;

/**
 * This class is for decompiling contents of method.
 * @author inagaki
 */
public class MethodDecompiler extends AbstractDecompiler {
    private OperandStack opStack;
    private LocalStack local;
    private String caller;
    private final int TRY     = 0x01;
    private final int CATCH   = 0x02;
    private final int FINALLY = 0x04;

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
                        local.push(args[i][1], type);
                        local.push(args[i][1], type);
                    } else {
                        local.push(args[i][1], type);
                    }
                }
                String type = args[length - 1][0];
                methodDeclaration.append(type).append(" ").append(args[length - 1][1]);
                if(type.matches("double|long")) {
                    local.push(args[length - 1][1], type);
                    local.push(args[length - 1][1], type);
                } else {
                    local.push(args[length - 1][1], type);
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

        // method body
        result.changeIndent(INCREMENT);
        println("\n>>> " + methodDeclaration + " <<<");
        buildMethodBody(method.getNodes(), method.getExContent());
        result.writeEndScope();
    }

    private void buildMethodBody(CFNode[] nodes, MethodContent.ExceptionContent except) {
        boolean typePop = true;
        Set<CFNode> incrementIgnoreNode = new HashSet<>();
        Set<CFNode> unindentSet = new HashSet<>();
        for(int i = 0; i < nodes.length; i++) {
            CFNode node = nodes[i];
            LineBuilder line = new LineBuilder();
            OpcodeInfo[] opcodes = node.getOpcodes().getAll();
            boolean addSemicolon = true;
            ConditionalExprBuilder builder = null;
            LoopStatementBuilder loop = null;

            if(unindentSet.contains(node)) {
                result.writeEndScope();
            }
            if(check(node, LoopEntry)) {
                loop = new LoopStatementBuilder();
            }
            if(check(node, Entry, OneLineEntry, OneLineEntryBreak)) {
                builder = new ConditionalExprBuilder();
                // There is jump point node of Entry-node in case of FALSE,
                // and there is case that the node is not Exit-node.
                // then, it doesn't unindent and add end scope to source code.
                // so, in the case, it holds FALSE-node,
                // and unindents and adds end scope at the time of processing FALSE-node.
                if(check(node, Entry)) {
                    unindentSet.add(getIfTerminal(i, node, nodes));
                }
            }
            boolean isCatch = (node.isTryCatchFinally() & CATCH) == CATCH;
            boolean isFinally = (node.isTryCatchFinally() & FINALLY) == FINALLY;
            // opcode analysis
            for(OpcodeInfo opcode : opcodes) {
//                println("[" + op.getPc() + "]" + opcode + " (catch: " + isCatch + ", finally: " + isFinally + ")");
//                println("local: " + local);
//                println("stack: " + opStack);
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
                        load(((IndexOpcode)opcode).getIndex()); break;
                    case iload_0:
                    case lload_0:
                    case fload_0:
                    case dload_0:
                        load(0); break;
                    case iload_1:
                    case lload_1:
                    case fload_1:
                    case dload_1:
                        load(1); break;
                    case iload_2:
                    case lload_2:
                    case fload_2:
                    case dload_2:
                        load(2); break;
                    case iload_3:
                    case lload_3:
                    case fload_3:
                    case dload_3:
                        load(3); break;
                    case aload:
                    case aload_0:
                    case aload_1:
                    case aload_2:
                    case aload_3:
                        load(getTailIndex(opcode)); break;
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
                    case lstore:
                    case dstore:
                        IndexOpcode inOp = (IndexOpcode)opcode;
                        line.append(getStored(inOp.getIndex()));
                        break;
                    case istore_0:
                    case fstore_0:
                    case lstore_0:
                    case dstore_0:
                        line.append(getStored(0)); break;
                    case istore_1:
                    case fstore_1:
                    case lstore_1:
                    case dstore_1:
                        line.append(getStored(1)); break;
                    case istore_2:
                    case fstore_2:
                    case lstore_2:
                    case dstore_2:
                        line.append(getStored(2)); break;
                    case istore_3:
                    case fstore_3:
                    case lstore_3:
                    case dstore_3:
                        line.append(getStored(3)); break;
                    case astore:
                    case astore_0:
                    case astore_1:
                    case astore_2:
                    case astore_3:
                        int number = getTailIndex(opcode);
                        if((isCatch | isFinally) && (local.size() > number)) {
                            break;
                        }
                        if(opStack.size() > 0) {
                            line.append(getStored(number));
                        }
                        break;
                    case iastore:
                    case lastore:
                    case fastore:
                    case dastore:
                    case aastore:
                    case bastore:
                    case castore:
                    case sastore:
                        String storingValue = opStack.pop(typePop);
                        String primIndex = opStack.pop(typePop);
                        String arrayRef = opStack.pop(typePop);
                        line.append(arrayRef, "[", primIndex, "]", " = ", storingValue);
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
                        String dup_x1_1  = opStack.pop();
                        String dup_x1_2  = opStack.pop();
                        String type_x1_1 = opStack.popType();
                        String type_x1_2 = opStack.popType();
                        opStack.push(dup_x1_1, type_x1_1);
                        opStack.push(dup_x1_2, type_x1_2);
                        opStack.push(dup_x1_1, type_x1_1);
                        break;
                    case dup_x2:
                        String dup_x2_1  = opStack.pop();
                        String dup_x2_2  = opStack.pop();
                        String dup_x2_3  = opStack.pop();
                        String type_x2_1 = opStack.popType();
                        String type_x2_2 = opStack.popType();
                        String type_x2_3 = opStack.popType();
                        opStack.push(dup_x2_1, type_x2_1);
                        opStack.push(dup_x2_3, type_x2_3);
                        opStack.push(dup_x2_2, type_x2_2);
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
                        String dup2_x1_1 = opStack.pop();
                        String dup2_x1_2 = opStack.pop();
                        String dup2_x1_3 = opStack.pop();
                        String type2_x1_1 = opStack.popType();
                        String type2_x1_2 = opStack.popType();
                        String type2_x1_3 = opStack.popType();
                        opStack.push(dup2_x1_1, type2_x1_1);
                        opStack.push(dup2_x1_2, type2_x1_2);
                        opStack.push(dup2_x1_3, type2_x1_3);
                        opStack.push(dup2_x1_1, type2_x1_1);
                        opStack.push(dup2_x1_2, type2_x1_2);
                        break;
                    case dup2_x2:
                        String dup2_x2_1 = opStack.pop();
                        String dup2_x2_2 = opStack.pop();
                        String dup2_x2_3 = opStack.pop();
                        String dup2_x2_4 = opStack.pop();
                        String type2_x2_1 = opStack.popType();
                        String type2_x2_2 = opStack.popType();
                        String type2_x2_3 = opStack.popType();
                        String type2_x2_4 = opStack.popType();
                        opStack.push(dup2_x2_1, type2_x2_1);
                        opStack.push(dup2_x2_2, type2_x2_2);
                        opStack.push(dup2_x2_4, type2_x2_4);
                        opStack.push(dup2_x2_3, type2_x2_3);
                        opStack.push(dup2_x2_2, type2_x2_2);
                        opStack.push(dup2_x2_1, type2_x2_1);
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
                        if(_const == 1)       line.append("++");
                        else if(_const == -1) line.append("--");
                        else if(_const > 1)   line.append(" += ").append(_const);
                        else if(_const < -1)  line.append(" -= ").append(_const);
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
                    case ifeq: // (x == y)
                        String eq = opStack.pop();
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            if(line.toString().matches("java\\.util\\.Iterator .+ = .+\\.iterator\\(\\)")) {
                                loop.accept(eq, opStack.popType(), " == ", node);
                            } else {
                                loop.accept("(" + line + ")", opStack.popType(), " == ", node);
                            }
                        } else {
                            builder.append(eq, opStack.popType(), " == ", node);
                        }
                        line.delete();
                        break;
                    case ifne: // (x != y)
                        String ne = opStack.pop();
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept("(" + line + ")", opStack.popType(), " != ", node);
                        } else {
                            builder.append(ne, opStack.popType(), " != ", node);
                        }
                        line.delete();
                        break;
                    case iflt: // (x < y)
                        String lt = opStack.pop();
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept("(" + line + ")", opStack.popType(), " < ", node);
                        } else {
                            builder.append(lt, opStack.popType(), " < ", node);
                        }
                        line.delete();
                        break;
                    case ifge: // (x >= y)
                        String ge = opStack.pop();
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept("(" + line + ")", opStack.popType(), " >= ", node);
                        } else {
                            builder.append(ge, opStack.popType(), " >= ", node);
                        }
                        line.delete();
                        break;
                    case ifgt: // (x > y)
                        String gt = opStack.pop();
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept("(" + line + ")", opStack.popType(), " < ", node);
//                            builder.append("(" + line + ")", opStack.popType(), " > ", node);
                        } else {
                            builder.append(gt, opStack.popType(), " > ", node);
                        }
                        line.delete();
                        break;
                    case ifle: // (x <= y)
                        String le = opStack.pop();
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept("(" + line + ")", opStack.popType(), " <= ", node);
//                            builder.append("(" + line + ")", opStack.popType(), " <= ", node);
                        } else {
                            builder.append(le, opStack.popType(), " <= ", node);
                        }
                        line.delete();
                        break;
                    case if_icmpeq:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" == ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" == ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_icmpne:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" != ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" != ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_icmplt:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" < ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" < ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_icmpge:
                        if(check(node, LoopEntry)) {
                            if(line.dumppedSize() > 1) {
                                loop.accept(line.getDumpped(), node);
                            } else {
                                loop.accept(line.toString(), node);
                            }
                            if(loop.isForEachArray()) {
                                // in case of "for(Type element : ARRAY)", 
                                // LoopExit node has incremental instruction.
                                // then, it is necessary to delete increment because
                                // the increment exists in source code.
                                incrementIgnoreNode.add(node);
                                unindentSet.add(node);
                            }
                            loop.accept(makeExprInt(" >= ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" >= ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_icmpgt:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" > ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" > ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_icmple:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" <= ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" <= ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_acmpeq:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" <= ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" <= ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case if_acmpne:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprInt(" != ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprInt(" != ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case _goto:
                        if(incrementIgnoreNode.contains(node.getChildren().iterator().next().getDest())) {
                            line.delete();
                            break;
                        }
                        if((line.length() == 0) && (opStack.size() > 0) && (node.getOpcodes().size() > 1)) {
                            // in case of opcodes size is equal to 1,
                            // that's not the Exit node which corresponds to Entry node.
                            OpcodeInfo end = node.getEnd();
                            if((end.getPc() == opcode.getPc())) {
                                line.append(opStack.pop(typePop));
                            }
                        }
                        if(check(node, OneLineEntryBreak)) {
                            line.append("break");
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
                        line.append("return ", opStack.pop(typePop));
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
                        // Xxx.yyy.FIELD|type
                        String[] getStaticField = getSta.getOperand().split("\\|");
                        opStack.push(getStaticField[0], getStaticField[1]);
                        break;
                    case putstatic:
                        CpRefOpcode putSta = (CpRefOpcode)opcode;
                        String putStaticField = putSta.getOperand().split("\\|")[0];
                        line.append(putStaticField, " = ", opStack.pop(typePop));
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
                        line.append(putCaller, ".", putNames[putNames.length - 1], " = ", value);
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
//                            if(opStack.size() > 0) {
//                                opStack.pop(typePop);
//                            }
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
                    case monitorenter:
                        line.delete();
                        line.append("synchronized(", opStack.pop(typePop), ") {");
                        addSemicolon = false;
                        break;
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
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprNull(" == ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprNull(" == ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case ifnonnull:
                        if(check(node, LoopEntry)) {
                            loop.accept(line.toString(), node);
                            loop.accept(makeExprNull(" != ", line.toString(), node), node);
                        } else {
                            builder.append(makeExprNull(" != ", line.toString(), node), node);
                        }
                        line.delete();
                        break;
                    case goto_w: break;
                    case jsr_w: break;
                    case breakpoint: break;
                    case impdep1: break;
                    case impdep2: break;
                    default: break;
                }
//                println("local["+local.size()+"]: " + local);
//                println("line:  " + line);
//                println("stack["+opStack.size()+"]: " + opStack);
            }

            // checking "else" block
            if((! isIfNode(node)) && (node.getParents().size() == 1)) {
                CFNode dominator = node.getImmediateDominator();
                // in general, when current node is "else" block,
                // the node is not Entry, OneLineEntry and OneLineEntryBreak.
                // in case of immediate dominator node of current node is
                // Entry, OneLineEntry or OneLineEntryBreak, current node is "else" block.
                // and, it is necessary to hold terminal node of if-else statement
                // for to unindent and add end scope of "else" block.
                CFNode terminal = getIfElseTerminal(dominator, nodes[i - 1]);
                if(terminal != null) {
                    unindentSet.add(terminal);
                    result.write("else {");
                    result.changeIndent(INCREMENT);
                }
            }

            if(check(node, LoopEntry)) {
                String loopDeclare = line.length() > 0 ? loop.build(line.toString()) : loop.build();
                if(loopDeclare.contains("#VAL#")) {
                    // [0]:type, [1]:name
                    String[] separated = line.toString().split(" ");
                    loopDeclare = loopDeclare.replace("#VAL#", separated[1]);
                }
                line = new LineBuilder();
                line.append(loopDeclare);
                addSemicolon = false;
            } else if(isIfNode(node)) {
                // elif block
                String processing = line.toString();
                line = new LineBuilder();
                if(isElifBlock(node)) {
                    line.append("else ");
                }
                line.append("if").append(builder.build()).append(" ");
                addSemicolon = checkNone(node, Entry);
                if(addSemicolon) {
                    line.append(processing);
                } else {
                    line.append("{"); 
                }
            }

            if(line.length() > 0 || (isCatch | isFinally)) {
                // When the node is not start or end of a statement, (ex. "if(...) {", "}")
                // add semicolon and write in the line context.
                if(addSemicolon) {
                    line.append(";");
                }
                if(checkNone(node, SynchronizedExit) && (except != null)) {
                    int tryCatchFinally = node.isTryCatchFinally();
                    int[][] table = except.getTable();
                    for(int[] column : table) {
                        int currentStart = node.getStart().getPc();
                        if(currentStart == column[0]) {
                            int target = column[2];
                            for(int targetIndex = i; targetIndex < nodes.length; targetIndex++) {
                                int start = nodes[targetIndex].getStart().getPc();
                                if((start == target)) {
                                    int flag  = nodes[targetIndex].isTryCatchFinally();
                                    if(((flag & (CATCH | FINALLY)) > 0)) {
                                        CFNode gotoNode = nodes[targetIndex - 1];
                                        if(check(gotoNode, Exit)) {
                                            CFNode gotoDest = gotoNode.getChildren().iterator().next().getDest();
                                            unindentSet.add(gotoDest);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(isFinally) {
                        // for finally-statement
                        result.write("finally {");
                        result.changeIndent(INCREMENT);
                        line.delete();
                        continue;
                    }
                    if(isCatch) {
                        // for catch-statement
                        StringJoiner exClass = new StringJoiner(" | ");
                        int start = node.getStart().getPc();
                        for(int j = 0; j < table.length; j++) {
                            if(table[j][2] == start) {
                                exClass.add(except.getException()[j]);
                            }
                        }
                        LineBuilder catchLine = new LineBuilder();
                        catchLine.append("catch(", exClass.toString(), " ");
                        catchLine.append(local.load(getTailIndex(opcodes[opcodes.length - 1])), ") {");
                        result.write(catchLine.toString());
                        result.changeIndent(INCREMENT);
                    }
                    if((tryCatchFinally & TRY) == TRY) {
                        // for try-statement
                        result.write("try {");
                        result.changeIndent(INCREMENT);
                    }
                    result.write(line.toString());
                }
            }
            indent(node, except);
        }
    }

    private int getTailIndex(OpcodeInfo opcode) {
        switch(opcode.getOpcodeType()) {
            case aload_0 :
            case astore_0: return 0;
            case aload_1 :
            case astore_1: return 1;
            case aload_2 :
            case astore_2: return 2;
            case aload_3 :
            case astore_3: return 3;
            case aload :
            case astore:   return ((IndexOpcode)opcode).getIndex();
            default:       return -1;
        }
    }

    /**
     * in case of specified node is terminal of if-else block, the node has more two parents.
     * and, (parents_size - 1) of the parents must be TrueBranch.
     */
    private boolean isElseBlock(CFNode ifElseTerminal) {
        if(ifElseTerminal == null) {
            return false;
        }
        int parentSize = ifElseTerminal.getParents().size();
        if(parentSize > 1) {
            int count = 0;
            for(CFEdge edge : ifElseTerminal.getParents()) {
                CFNode dest = edge.getDest();
                if(check(dest, OneLineEntry, OneLineEntryBreak, Exit)) {
                    count++;
                }
            }
            return count == parentSize - 1;
        }
        return false;
    }

    private boolean isElifBlock(CFNode node) {
        if(node.getParents().size() == 1) {
            CFNode dominator = node.getImmediateDominator();
            if(isIfNode(dominator)) {
                CFEdge edge = node.getParents().iterator().next();
                if(check(node,Entry)) {
                    // this node has Entry node as immediate dominator node.
                    // in addition, this node connects to the Entry node with FalseBranch.
                    // then, this node is "else-if" block.
                    return edge.getType() == FalseBranch;
                } else {
                    // this node has two nodes in case of if-statement is "true" and "false".
                    // then, this node is "else-if" block.
                    return dominator.getChildren().size() == 2;
                }
            }
        }
        return false;
    }

    private boolean isIfNode(CFNode node) {
        return check(node, Entry, OneLineEntry, OneLineEntryBreak);
    }

    private void indent(CFNode node, MethodContent.ExceptionContent except) {
        if(check(node, LoopEntry, Entry, SynchronizedEntry)) {
            result.changeIndent(INCREMENT);
        }
        if(check(node, LoopExit, Exit, SynchronizedExit)) {
//            println("unindent: " + node.getType());
            result.writeEndScope();
//            println("unindented");
        }
    }

    private String makeExprInt(String operator, String line, CFNode node) {
        String value_2 = opStack.pop(true);
        String value_1 = opStack.pop(true);
        if(check(node, LoopEntry) && line.contains(" = ")) {
            String[] storeLine = line.split(" = ");
            // ex). ((x = ??) > y)
            if(storeLine[1].equals(value_1)) {
                return "((" + line + ")" + operator + value_2 + ")";
            } else if(storeLine[1].equals(value_2)) {
                return "(" + value_1 + operator + "(" + line + "))";
            }
        }
        return "(" + value_1 + operator + value_2 + ")";
    }

    private String makeExprNull(String operator, String line, CFNode node) {
        String value = opStack.pop(true);
        if(check(node, LoopEntry)) {
            // ex). ((val = ??) == null)
            return "((" + line + ")" + operator + "null)";
        }
        return "(" + value + operator + "null)";
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

    private CFNode getIfTerminal(int index, CFNode target, CFNode[] nodes) {
        for(CFEdge edge : target.getChildren()) {
            if(edge.getType() == FalseBranch) {
                CFNode terminal = edge.getDest();
                while(! nodes[index].equals(terminal)) {
                    index++;
                }
                if(checkNone(nodes[index - 1], Exit)) {
                    return terminal;
                }
            }
        }
        return null;
    }

    private CFNode getIfElseTerminal(CFNode dominator, CFNode before) {
        CFNode ifElseTerminal = null;
        if(isIfNode(dominator)) {
            if(check(before, Exit)) {
                ifElseTerminal = before.getChildren().iterator().next().getDest();
            } else {
                for(CFEdge edge : dominator.getChildren()) {
                    if(edge.getType() == TrueBranch) {
                        ifElseTerminal = edge.getDest();
                    }
                }
            }
        }
        return isElseBlock(ifElseTerminal) ? ifElseTerminal : null;
    }

    /**
     * this method is for invoke method instruction.
     * 
     * push element onto operand stack
     * when specified opcode is end in current node has all opcodes.
     * 
     * in case of the opcode is end,
     * it is necessary to push element onto openrand stack
     * because there are some processing in the next.
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

    private class LineBuilder {
        private StringBuilder sb;
        private List<String> list;

        public LineBuilder() {
            this.sb = new StringBuilder();
            this.list = new ArrayList<>();
        }

        public int dumppedSize() {
            return list.size();
        }

        public String[] getDumpped() {
            return list.toArray(new String[0]);
        }

        public StringBuilder append(String s) {
            list.add(s);
            return sb.append(s);
        }

        public StringBuilder append(String... strs) {
            StringBuilder sb = new StringBuilder();
            for(String s : strs) {
                sb.append(s);
            }
            list.add(sb.toString());
            return this.sb.append(sb.toString());
        }

        public void delete() {
            list.clear();
            sb.delete(0, sb.length());
        }

        public int length() {
            return sb.length();
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}