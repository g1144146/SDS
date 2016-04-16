package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.multianewarray">
 * multianewarray
 * </a>.
 * @author inagaki
 */
public class MultiANewArray extends CpRefOpcode {
	private int dimensions;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public MultiANewArray(int pc) {
		super(MnemonicTable.multianewarray, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		this.dimensions = raf.readByte();
	}

	/**
	 * return value of dimensions of the array.
	 * @return value of dimensions of the array
	 */
	public int getDemensions() {
		return dimensions;
	}
}