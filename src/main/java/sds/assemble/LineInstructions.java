package sds.assemble;

import sds.classfile.attributes.LineNumberTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;

/**
 *
 * @author inagaki
 */
public class LineInstructions {
	private LineNumberTable.LNTable table;
	private Opcodes opcodes;

	public LineInstructions(LineNumberTable.LNTable table) {
		this.table = table;
		this.opcodes = new Opcodes();
	}

	public void addOpcode(OpcodeInfo opcode) {
		opcodes.add(opcode.getPc(), opcode);
	}

	public LineNumberTable.LNTable getTable() {
		return table;
	}

	public Opcodes getOpcodes() {
		return opcodes;
	}

	public boolean isInPcRange(int pc) {
		return table.getStartPc() <= pc && pc <= table.getEndPc();
	}
}