package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class CpRefOpcode extends OpcodeInfo {
	int index;

	public CpRefOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		if(opcodeType == MnemonicTable.ldc) {
			this.index = raf.readUnsignedByte();
		} else {
			this.index = raf.readShort();
		}
	}

	public int getIndexByte() {
		return index;
	}
}
