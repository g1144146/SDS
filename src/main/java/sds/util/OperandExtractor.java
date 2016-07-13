package sds.util;

import sds.classfile.ConstantPool;
import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.CpRefOpcode;
import sds.classfile.bytecode.Iinc;
import sds.classfile.bytecode.IndexOpcode;
import sds.classfile.bytecode.InvokeDynamic;
import sds.classfile.bytecode.InvokeInterface;
import sds.classfile.bytecode.LookupSwitch;
import sds.classfile.bytecode.MultiANewArray;
import sds.classfile.bytecode.NewArray;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.PushOpcode;
import sds.classfile.bytecode.TableSwitch;
import sds.classfile.bytecode.Wide;

import static sds.util.Utf8ValueExtractor.extract;

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
	public static String extractOperand(OpcodeInfo op, ConstantPool pool) {
		StringBuilder operand = new StringBuilder("");
		if(op instanceof PushOpcode) {
			PushOpcode push = (PushOpcode)op;
			operand.append(push.getValue());
		} else if(op instanceof BranchOpcode) {
			BranchOpcode branch = (BranchOpcode)op;
			operand.append(branch.getBranch());
		} else if(op instanceof Iinc) {
			Iinc iinc = (Iinc)op;
			operand.append(iinc.getIndex())
				.append(",").append(iinc.getConst());
		} else if(op instanceof IndexOpcode) {
			IndexOpcode io = (IndexOpcode)op;
			operand.append(io.getIndex());
		} else if(op instanceof InvokeDynamic) {
			InvokeDynamic id = (InvokeDynamic)op;
			operand.append(extract(pool.get(id.getIndexByte()-1), pool));
		} else if(op instanceof InvokeInterface) {
			InvokeInterface ii = (InvokeInterface)op;
			operand.append(ii.getCount())
				.append(",").append(extract(pool.get(ii.getIndexByte()-1), pool));
		} else if(op instanceof LookupSwitch) {
			LookupSwitch ls = (LookupSwitch)op;
			int[] match = ls.getMatch();
			int[] offset = ls.getOffset();
			operand.append("[");
			for(int i = 0; i < match.length - 1; i++) {
				operand.append(match[i]).append(":").append(offset[i]+ls.getPc()).append(",");
			}
			operand.append(match[match.length-1]).append(":")
				.append(offset[match.length-1]+ls.getPc()).append("],");
			operand.append(ls.getDefault()+ls.getPc());
		} else if(op instanceof MultiANewArray) {
			MultiANewArray mana = (MultiANewArray)op;
			operand.append(extract(pool.get(mana.getIndexByte()-1), pool));
		} else if(op instanceof NewArray) {
			NewArray na = (NewArray)op;
			operand.append(na.getType());
		} else if(op instanceof TableSwitch) {
			TableSwitch ts = (TableSwitch)op;
			int[] jump = ts.getJumpOffsets();
			operand.append("[");
			for(int i = 0; i < jump.length - 1; i++) {
				operand.append(jump[i]+ts.getPc()).append(",");
			}
			operand.append(jump[jump.length-1]+ts.getPc()).append("],");
			operand.append(ts.getDefault()+ts.getPc());
		} else if(op instanceof Wide) {
			Wide wide = (Wide)op;
			operand.append(wide.getConst()).append(",")
				.append(extract(pool.get(wide.getIndexByte()-1), pool));
		} else if(op instanceof CpRefOpcode) {
			CpRefOpcode cp = (CpRefOpcode)op;
			operand.append(extract(pool.get(cp.getIndexByte()-1), pool));
		}
		return operand.toString();
	}
}