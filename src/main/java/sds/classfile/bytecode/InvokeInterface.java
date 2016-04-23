package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokeinterface">
 * invokeinterface
 * </a>.
 * @author inagaki
 */
public class InvokeInterface extends CpRefOpcode {
	private int count;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public InvokeInterface(int pc) {
		super(MnemonicTable.invokeinterface, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		this.count = raf.readUnsignedByte();
	}

	/**
	 * returns count.
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + count;
	}
}