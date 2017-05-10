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
            operand.append(push.getValue());
        } else if(op instanceof BranchOpcode) {
            BranchOpcode branch = (BranchOpcode)op;
            operand.append(branch.getBranch());
        } else if(op instanceof Iinc) {
            Iinc iinc = (Iinc)op;
            operand.append(iinc.getIndex(), ",", iinc.getConst());
        } else if(op instanceof IndexOpcode) {
            IndexOpcode io = (IndexOpcode)op;
            operand.append(io.getIndex());
        } else if(op instanceof InvokeDynamic) {
            InvokeDynamic id = (InvokeDynamic)op;
            operand.append(extract(id.getIndexByte(), pool));
        } else if(op instanceof InvokeInterface) {
            InvokeInterface ii = (InvokeInterface)op;
            operand.append(extract(ii.getIndexByte(), pool));
        } else if(op instanceof LookupSwitch) {
            LookupSwitch ls = (LookupSwitch)op;
            int[] match = ls.getMatch();
            int[] offset = ls.getOffset();
            operand.append("[");
            for(int i = 0; i < match.length - 1; i++) {
                operand.append(match[i], ":", offset[i]+ls.getPc(), ",");
            }
            operand.append(match[match.length-1], ":", offset[match.length-1]+ls.getPc(), "],");
            operand.append(ls.getDefault()+ls.getPc());
        } else if(op instanceof MultiANewArray) {
            MultiANewArray mana = (MultiANewArray)op;
            operand.append(extract(mana.getIndexByte(), pool));
        } else if(op instanceof NewArray) {
            NewArray na = (NewArray)op;
            operand.append(na.getType());
        } else if(op instanceof TableSwitch) {
            TableSwitch ts = (TableSwitch)op;
            int[] jump = ts.getJumpOffsets();
            operand.append("[");
            for(int i = 0; i < jump.length - 1; i++) {
                operand.append(jump[i]+ts.getPc(), ",");
            }
            operand.append(jump[jump.length-1]+ts.getPc(), "],");
            operand.append(ts.getDefault()+ts.getPc());
        } else if(op instanceof Wide) {
            Wide wide = (Wide)op;
            operand.append(wide.getConst(), ",", extract(wide.getIndexByte(), pool));
        } else if(op instanceof CpRefOpcode) {
            CpRefOpcode cp = (CpRefOpcode)op;
            operand.append(extract(cp.getIndexByte(), pool));
        }
        return operand.toString();
    }
}