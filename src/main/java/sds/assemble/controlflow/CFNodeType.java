package sds.assemble.controlflow;

import sds.classfile.bytecode.BranchOpcode;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.classfile.bytecode.SwitchOpcode;

/**
 * This enum class is for type of {@link CFNode <code>CFNode</code>}.
 * @author inagaki
 */
public enum CFNodeType {
	Normal,
	Entry,
	Exit,
	LoopEntry,
	LoopExit;

	/**
	 * returns type of control flow node.
	 * @param opcodes opcode sequence
	 * @return node type
	 */
	public static CFNodeType getType(Opcodes opcodes) {
		OpcodeInfo op = opcodes.getMap().lastEntry().getValue();
		if(op instanceof BranchOpcode) {
			BranchOpcode branch = (BranchOpcode)op;
			switch(branch.getOpcodeType()) {
				case jsr:
				case jsr_w:
					// todo
					break;
				case _goto:
				case goto_w:
					if(branch.getBranch() > branch.getPc())
						return Exit;
					else
						return LoopExit;
				default:
					return Entry;
			}
		} else if(op instanceof SwitchOpcode) {
			return Entry;
		}
		return Normal;
	}
}