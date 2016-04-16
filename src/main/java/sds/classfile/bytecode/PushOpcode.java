package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for opcode has push value.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.bipush">
 * bipush
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.sipush">
 * sipush
 * </a>
 * @author inagaki
 */
public class PushOpcode extends OpcodeInfo {
	int value;

	/**
	 * constructor.
	 * @param opcodeType opcode type
	 * @param pc index into the code array
	 */
	public PushOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		if(this.getOpcodeType() == MnemonicTable.bipush) {
			this.value = raf.readByte();
		} else if(this.getOpcodeType() == MnemonicTable.sipush) {
			this.value = raf.readShort();
		}
	}

	/**
	 * returns push value.
	 * @return push value
	 */
	public int getValue() {
		return value;
	}
}