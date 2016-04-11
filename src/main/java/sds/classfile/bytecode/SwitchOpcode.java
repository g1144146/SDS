package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
abstract class SwitchOpcode extends OpcodeInfo {
	
	int defaultByte;

	public SwitchOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		for(int i = 1; ((i+pc) % 4) != 0; i++) {
			raf.readByte();
		}
//		raf.skipBytes((this.pc % 4));
		this.defaultByte = raf.readInt();
	}

	public int getDefault() {
		return defaultByte;
	}
}
