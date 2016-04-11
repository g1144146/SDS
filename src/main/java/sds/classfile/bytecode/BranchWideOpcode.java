package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class BranchWideOpcode extends BranchOpcode {

	public BranchWideOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.branch = raf.readInt();
	}
}
