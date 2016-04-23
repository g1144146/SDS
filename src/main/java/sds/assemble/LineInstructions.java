package sds.assemble;

import sds.classfile.attributes.LineNumberTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;

/**
 * This class is for instructions of a line of method.
 * @author inagaki
 */
public class LineInstructions {
	private LineNumberTable.LNTable table;
	private Opcodes opcodes;

	/**
	 * constructor.
	 * @param table line number table 
	 */
	public LineInstructions(LineNumberTable.LNTable table) {
		this.table = table;
		this.opcodes = new Opcodes();
	}

	/**
	 * adds opcode.
	 * @param opcode opcode 
	 */
	public void addOpcode(OpcodeInfo opcode) {
		opcodes.add(opcode.getPc(), opcode);
	}

	/**
	 * returns line number table.
	 * @return line number table
	 */
	public LineNumberTable.LNTable getTable() {
		return table;
	}

	/**
	 * returns opcodes of this line.
	 * @return opcodes
	 */
	public Opcodes getOpcodes() {
		return opcodes;
	}

	/**
	 * returns opcode from specified pc.
	 * @param pc index into the code array.
	 * @return opcode
	 */
	public OpcodeInfo get(int pc) {
		return opcodes.get(pc);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(OpcodeInfo op : opcodes.getMap().values()) {
			sb.append(op.getPc()).append(":").append(op).append("\n");
		}
		return sb.toString();
	}
}