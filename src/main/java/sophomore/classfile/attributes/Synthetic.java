package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Synthetic extends AttributeInfo {

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public Synthetic(int nameIndex, int length) {
		super(AttributeType.Synthetic, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		// do nothing.
	}
}
