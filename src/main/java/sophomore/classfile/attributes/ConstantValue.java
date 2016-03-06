package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class ConstantValue extends AttributeInfo {
	/**
	 * 
	 */
	int constantValueIndex;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public ConstantValue(int nameIndex, int length) {
		super(AttributeType.Type.ConstantValue, nameIndex, length);
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
