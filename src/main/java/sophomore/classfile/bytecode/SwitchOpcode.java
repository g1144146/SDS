package sophomore.classfile.bytecode;

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
		raf.skipBytes(3);
		this.defaultByte = raf.readInt();
	}

	public int getDefault() {
		return defaultByte;
	}
}
