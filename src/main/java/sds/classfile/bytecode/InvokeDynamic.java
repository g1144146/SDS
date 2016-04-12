package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class InvokeDynamic extends CpRefOpcode {

	public InvokeDynamic(int pc) {
		super(MnemonicTable.inovokedynamic, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		raf.skipBytes(2);
	}
}
