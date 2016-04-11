package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class ConstantValue extends AttributeInfo {
	/**
	 * The value of the constantvalue_index item must be a valid index into the constant_pool table.
	 * The constant_pool entry at that index gives the constant value represented by this attribute.
	 * The constant_pool entry must be of a type appropriate to the field, as specified in Table 4.7.2-A.
	 */
	int constantValueIndex;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public ConstantValue(int nameIndex, int length) {
		super(AttributeType.ConstantValue, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public int getConstantValueIndex() {
		return constantValueIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.constantValueIndex = raf.readShort();
	}
}
