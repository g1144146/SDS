package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class BranchOpcode extends OpcodeInfo {
	/**
	 * 
	 */
	int branch;

	public BranchOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.branch = raf.readUnsignedShort();
	}

	public int getBranch() {
		return branch;
	}
}
