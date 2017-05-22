package sds.classfile.bytecode;

import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 *
 * @author inagaki
 */
public class OperandExtractor {
    /**
     * returns operand which opcode has.
     * @param op opcode
     * @param pool constant-pool
     * @return operand
     */
    public static String extractOperand(OpcodeInfo op, ConstantInfo[] pool) {
        SDSStringBuilder operand = new SDSStringBuilder("");
        if(op instanceof PushOpcode) {
            PushOpcode push = (PushOpcode)op;
            operand.append(push.value);
        } else if(op instanceof BranchOpcode) {
            BranchOpcode branch = (BranchOpcode)op;
            operand.append(branch.branch);
        } else if(op instanceof Iinc) {
            Iinc iinc = (Iinc)op;
            operand.append(iinc.index, ",", iinc._const);
        } else if(op instanceof IndexOpcode) {
            IndexOpcode io = (IndexOpcode)op;
            operand.append(io.index);
        } else if(op instanceof InvokeDynamic) {
            InvokeDynamic id = (InvokeDynamic)op;
            operand.append(extract(id.index, pool));
        } else if(op instanceof InvokeInterface) {
            InvokeInterface ii = (InvokeInterface)op;
            operand.append(extract(ii.index, pool));
        } else if(op instanceof LookupSwitch) {
            LookupSwitch ls = (LookupSwitch)op;
            int[] match = ls.match;
            int[] offset = ls.offset;
            operand.append("[");
            for(int i = 0; i < match.length - 1; i++) {
                operand.append(match[i], ":", offset[i] + ls.pc, ",");
            }
            operand.append(match[match.length - 1], ":", offset[match.length-1] + ls.pc, "],");
            operand.append(ls.getDefault()+ls.pc);
        } else if(op instanceof MultiANewArray) {
            MultiANewArray mana = (MultiANewArray)op;
            operand.append(extract(mana.index, pool));
        } else if(op instanceof NewArray) {
            NewArray na = (NewArray)op;
            operand.append(na.atype);
        } else if(op instanceof TableSwitch) {
            TableSwitch ts = (TableSwitch)op;
            int[] jump = ts.jumpOffsets;
            operand.append("[");
            for(int i = 0; i < jump.length - 1; i++) {
                operand.append(jump[i] + ts.pc, ",");
            }
            operand.append(jump[jump.length-1] + ts.pc, "],");
            operand.append(ts.getDefault() + ts.pc);
        } else if(op instanceof Wide) {
            Wide wide = (Wide)op;
            operand.append(wide.constByte, ",", extract(wide.getIndexByte(), pool));
        } else if(op instanceof CpRefOpcode) {
            CpRefOpcode cp = (CpRefOpcode)op;
            operand.append(extract(cp.index, pool));
        }
        return operand.toString();
    }
}