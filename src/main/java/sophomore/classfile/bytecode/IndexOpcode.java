package sophomore.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class IndexOpcode extends OpcodeInfo {
	int index;
	public IndexOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.index = raf.readByte();
	}

	public int getIndex() {
		return index;
	}
}
