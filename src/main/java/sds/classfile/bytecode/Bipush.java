package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Bipush extends OpcodeInfo {
	byte b;
	public Bipush(int pc) {
		super(MnemonicTable.bipush, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.b = raf.readByte();
	}

	public byte getByte() {
		return b;
	}
}
