package sophomore.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class MultiANewArray extends CpRefOpcode {

	int dimensions;

	public MultiANewArray(int pc) {
		super(MnemonicTable.multianewarray, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		this.dimensions = raf.readByte();
	}
}
