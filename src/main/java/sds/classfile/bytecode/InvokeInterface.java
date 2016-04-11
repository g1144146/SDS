package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class InvokeInterface extends CpRefOpcode {

	int count;

	public InvokeInterface(int pc) {
		super(MnemonicTable.invokeinterface, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		this.count = raf.readUnsignedByte();
	}

	public int getCount() {
		return count;
	}
}
