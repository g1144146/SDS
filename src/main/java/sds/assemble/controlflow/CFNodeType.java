package sds.assemble.controlflow;

import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.MnemonicTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.classfile.bytecode.SwitchOpcode;

import static sds.classfile.bytecode.MnemonicTable._goto;
import static sds.classfile.bytecode.MnemonicTable.goto_w;
import static sds.classfile.bytecode.MnemonicTable.jsr;
import static sds.classfile.bytecode.MnemonicTable.jsr_w;
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
	OneLineEntry,
	OneLineEntryBreak,
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
		CFNodeType type;
		if((type = searchTypeFromOpcodes(opcodes, end)) != Normal) {
			return type;
		}

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

	private static CFNodeType searchTypeFromOpcodes(Opcodes opcodes, OpcodeInfo end) {
		int switchCount = 0;
		int ifCount = 0;
		int gotoCount = 0;
		int synchCount = 0;
		int synchExitCount = 0;
		for(OpcodeInfo info : opcodes.getAll()) {
			if(info instanceof SwitchOpcode) {
				switchCount++;
			} else if(info instanceof BranchOpcode) {
				MnemonicTable type = info.getOpcodeType();
				if((type == _goto) || (type == goto_w)) {
					gotoCount++;
				} else if((type == jsr) || (type == jsr_w)) {
					// todo
				} else {
					ifCount++;
				}
			} else if(info.getOpcodeType() == monitorenter) {
				synchCount++;
			} else if(info.getOpcodeType() == monitorexit) {
				synchExitCount++;
			}
		}

		if(switchCount    > 0) return (switchCount > 1) ? StringSwitch : Switch;
		if(synchCount     > 0) return SynchronizedEntry;
		if(synchExitCount > 0) return SynchronizedExit;

		if(ifCount > 0) {
			if((end instanceof BranchOpcode) && (searchBranchType(end) == Entry)) {
				return Entry;
			}
			// in case of "if(XXX) break;", opcode sequence is next:
			// op_1, op_2, ..., if_xx, goto
			int size = opcodes.size();
			OpcodeInfo beforeLast = opcodes.getAll()[size - 2];
			if(gotoCount > 0) {
				if((beforeLast instanceof BranchOpcode) && (searchBranchType(beforeLast) == Entry)) {
					return OneLineEntryBreak;
				}
			}
			return OneLineEntry;
		}
		if(gotoCount > 0) {
			// Exit or LoopExit
			return searchBranchType(end);
		}
		return Normal;
	}
	
	private static CFNodeType searchType(OpcodeInfo op) {
		if(op instanceof BranchOpcode) return searchBranchType(op);
		if(isReturn(op))               return End;
		return Normal;
	}

	private static CFNodeType searchBranchType(OpcodeInfo op) {
		BranchOpcode branch = (BranchOpcode)op;
		switch(branch.getOpcodeType()) {
			case jsr:
			case jsr_w:
				// todo
				throw new RuntimeException("it doesn't cope with jsr.");
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