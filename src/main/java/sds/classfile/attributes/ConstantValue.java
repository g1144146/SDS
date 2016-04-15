package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.2">ConstantValue Attribute</a>.
 * @author inagaki
 */
public class ConstantValue extends AttributeInfo {
	private int constantValueIndex;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public ConstantValue(int nameIndex, int length) {
		super(AttributeType.ConstantValue, nameIndex, length);
	}

	/**
	 * returns constant-pool entry index of constant value.
	 * @return constant-pool entry index of constant value
	 */
	public int getConstantValueIndex() {
		return constantValueIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.constantValueIndex = raf.readShort();
	}
}
