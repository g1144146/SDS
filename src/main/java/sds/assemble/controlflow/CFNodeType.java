package sds.assemble.controlflow;

import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.classfile.bytecode.SwitchOpcode;

/**
 * This enum class is for type of
 * {@link CFNode <code>CFNode</code>}.
 * @author inagaki
 */
public enum CFNodeType {
	Normal,
	Entry,
	Exit,
	Switch,
	LoopEntry,
	LoopExit,
	End;
	
	/**
	 * returns type of control flow node.
	 * @param opcodes opcode sequence
	 * @param end end od opcode
	 * @return node type
	 */
	public static CFNodeType getType(Opcodes opcodes, OpcodeInfo end) {
		CFNodeType type;
		if((type = searchType(end)) != Normal) {
			return type;
		}
		
		// processing for for-each statement.
		int index = 0;
		int[] keys = opcodes.getKeys();
		while((index < keys.length) && (type = searchType(opcodes.get(keys[index]))) == Normal) {
			index++;
		}
		return type;
	}
	
	private static CFNodeType searchType(OpcodeInfo op) {
		if(op instanceof BranchOpcode) {
			BranchOpcode branch = (BranchOpcode)op;
			switch(branch.getOpcodeType()) {
				case jsr:
				case jsr_w:
					// todo
					break;
				case _goto:
				case goto_w:
					if(branch.getBranch() > 0) return Exit;
					else                       return LoopExit;
				default:
					return Entry;
			}
		} else if(op instanceof SwitchOpcode) {
			return Switch;
		}
		return isReturn(op) ? End : Normal;
	}
	
	private static boolean isReturn(OpcodeInfo op) {
		switch(op.getOpcodeType()) {
			case _return:
			case areturn:
			case ireturn:
			case freturn:
			case lreturn:
			case dreturn: return true;
			default:      return false;
		}
	}
}