package sds.assemble.controlflow;

import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.classfile.bytecode.SwitchOpcode;

import static sds.classfile.bytecode.MnemonicTable.monitorenter;
import static sds.classfile.bytecode.MnemonicTable.monitorexit;

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
	StringSwitch,
	SynchronizedEntry,
	SynchronizedExit,
	LoopEntry,
	LoopExit,
	End;
	
	/**
	 * returns type of control flow node.
	 * @param opcodes opcode sequence
	 * @param end end of opcode
	 * @return node type
	 */
	public static CFNodeType getType(Opcodes opcodes, OpcodeInfo end) {
		if(hasSomeSwitchOpcode(opcodes)) {
			return StringSwitch;
		}

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

	private static boolean hasSomeSwitchOpcode(Opcodes opcodes) {
		int count = 0;
		for(OpcodeInfo info : opcodes.getAll()) {
			if(info instanceof SwitchOpcode) {
				count++;
			}
		}
		return count > 1;
	}
	
	private static CFNodeType searchType(OpcodeInfo op) {
		if(op instanceof BranchOpcode) {
			return searchBranchType(op);
		}
		if(op instanceof SwitchOpcode) {
			return Switch;
		}
		if(op.getOpcodeType() == monitorenter) {
			return SynchronizedEntry;
		}
		if(op.getOpcodeType() == monitorexit) {
			return SynchronizedExit;
		}
		return isReturn(op) ? End : Normal;
	}

	private static CFNodeType searchBranchType(OpcodeInfo op) {
		BranchOpcode branch = (BranchOpcode)op;
		switch(branch.getOpcodeType()) {
			case jsr:
			case jsr_w:
				// todo
				break;
			case _goto:
			case goto_w:
				if(branch.getBranch() > 0) {
					return Exit;
				}
				return LoopExit;
		}
		return Entry;
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
