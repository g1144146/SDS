package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.Info;

/**
 * This class is for opcode has no operand.
 * @author inagaki
 */
public class OpcodeInfo implements Info {
	private MnemonicTable opcodeType;
	private int pc;

	OpcodeInfo(MnemonicTable opcodeType, int pc) {
		this.opcodeType = opcodeType;
		this.pc = pc;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {}

	/**
	 * returns opcode type.
	 * @return opcode type.
	 */
	public MnemonicTable getOpcodeType() {
		return opcodeType;
	}

	/**
	 * returns index into the code array.
	 * @return index
	 */
	public int getPc() {
		return pc;
	}

	@Override
	public String toString() {
		return opcodeType.toString();
	}
}