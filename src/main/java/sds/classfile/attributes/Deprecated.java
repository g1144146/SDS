package sds.classfile.attributes;

import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.15">Deprecated Attribute</a>.
 * @author inagaki
 */
public class Deprecated extends AttributeInfo {
	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public Deprecated(int nameIndex, int length) {
		super(AttributeType.Deprecated, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws Exception {
		// do nothing.
	}
}