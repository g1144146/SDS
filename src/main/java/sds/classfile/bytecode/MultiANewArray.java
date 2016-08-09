package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;

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
	public void read(ClassFileStream data) throws IOException {
		super.read(data);
		this.dimensions = data.readByte();
	}

	/**
	 * return value of dimensions of the array.
	 * @return value of dimensions of the array
	 */
	public int getDemensions() {
		return dimensions;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + dimensions;
	}
}