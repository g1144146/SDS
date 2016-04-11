package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Deprecated extends AttributeInfo {
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public Deprecated(int nameIndex, int length) {
		super(AttributeType.Deprecated, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		// do nothing.
	}
}
