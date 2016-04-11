package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Sipush extends OpcodeInfo {
	Short s;
	public Sipush(int pc) {
		super(MnemonicTable.sipush, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.s = raf.readShort();
	}

	public Short getShort() {
		return s;
	}
}
