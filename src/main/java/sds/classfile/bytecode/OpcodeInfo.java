package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.Info;

/**
 * This class is for opcode that has no operand.
 * @author inagaki
 */
public class OpcodeInfo implements Info {
	/**
	 * 
	 */
	MnemonicTable opcodeType;
	/**
	 * 
	 */
	int pc;

	OpcodeInfo(MnemonicTable opcodeType, int pc) {
		this.opcodeType = opcodeType;
		this.pc = pc;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {}

	public MnemonicTable getOpcodeType() {
		return opcodeType;
	}

	public int getPc() {
		return pc;
	}

	@Override
	public String toString() {
		return opcodeType.toString();
	}
}