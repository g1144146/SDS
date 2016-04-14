package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.8">Synthetic Attribute</a>.
 * @author inagaki
 */
public class Synthetic extends AttributeInfo {

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public Synthetic(int nameIndex, int length) {
		super(AttributeType.Synthetic, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		// do nothing.
	}
}
