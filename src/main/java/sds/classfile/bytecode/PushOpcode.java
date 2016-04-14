package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class PushOpcode extends OpcodeInfo {
	/**
	 * 
	 */
	int value;

	/**
	 * 
	 * @param opcodeType
	 * @param pc 
	 */
	public PushOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		if(opcodeType == MnemonicTable.bipush) {
			this.value = raf.readByte();
		} else if(opcodeType == MnemonicTable.sipush) {
			this.value = raf.readShort();
		}
	}

	/**
	 * 
	 * @return 
	 */
	public int getValue() {
		return value;
	}
}
