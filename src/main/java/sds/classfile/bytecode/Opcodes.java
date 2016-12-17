package sds.classfile.bytecode;

import java.util.Arrays;

/**
 * This class is for
 * {@link OpcodeInfo <code>OpcodeInfo</code>}
 * sequence of method.
 * @author inagaki
 */
public class Opcodes {
	private OpcodeInfo[] opcodes;
	private int size;

	/**
	 * constructor.
	 */
	public Opcodes() {
		this.opcodes = new OpcodeInfo[10];
	}

	/**
	 * returns opcode map size.
	 * @return size
	 */
	public int size() {
		return size;
	}

	/**
	 * adds opcode to map.
	 * @param element opcode
	 */
	public void add(OpcodeInfo element) {
		if(size >= opcodes.length) {
			OpcodeInfo[] newInfo = new OpcodeInfo[opcodes.length + 5];
			System.arraycopy(opcodes, 0, newInfo, 0, opcodes.length);
			opcodes = newInfo;
		}
		opcodes[size] = element;
		size++;
	}

	/**
	 * returns opcode of specified pc.
	 * @param pc index into the code array
	 * @return opcode
	 */
	public OpcodeInfo get(int pc) {
		for(int i = 0; i < size; i++) {
			if(pc == opcodes[i].getPc()) {
				return opcodes[i];
			}
		}
		return null;
	}

	/**
	 * returns opcodes.
	 * @return opcodes.
	 */
	public OpcodeInfo[] getAll() {
		return Arrays.copyOfRange(opcodes, 0, size);
	}

	/**
	 * returns values of pc of opcode.
	 * @return values
	 */
	public int[] getKeys() {
		int[] pc = new int[size];
		for(int i = 0; i < size; i++) {
			pc[i] = opcodes[i].getPc();
		}
		return pc;
	}
}