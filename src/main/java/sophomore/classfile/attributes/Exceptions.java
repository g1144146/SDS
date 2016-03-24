package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Exceptions extends AttributeInfo {
	/**
	 * The constant_pool entry at that index must be a CONSTANT_Class_info structure.
	 */
	int[] exceptionIndexTable;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public Exceptions(int nameIndex, int length) {
		super(AttributeType.Exceptions, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public int[] getExceptionIndexTable() {
		return exceptionIndexTable;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.exceptionIndexTable = new int[len];
		for(int i = 0; i < len; i++) {
			exceptionIndexTable[i] = raf.readShort();
		}
	}
}
