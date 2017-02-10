package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import sds.classfile.Info;

/**
 * This class is for opcode has no operand.
 * @author inagaki
 */
public class OpcodeInfo implements Info {
	private int pc;
	MnemonicTable opcodeType;

	OpcodeInfo(MnemonicTable opcodeType, int pc) {
		this.opcodeType = opcodeType;
		this.pc = pc;
	}

	/**
	 * reads info from classfile.<br>
	 * method for opcode info.
	 * @param data classfile stream
	 * @throws IOException 
	 */
	public void read(ClassFileStream data) throws IOException {}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		read(data);
	}

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