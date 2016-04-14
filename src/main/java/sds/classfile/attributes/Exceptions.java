package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.5">Exceptions Attribute</a>.
 * @author inagaki
 */
public class Exceptions extends AttributeInfo {
	/**
	 * constant-pool entry indexes of exception classes.
	 */
	int[] exceptionIndexTable;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public Exceptions(int nameIndex, int length) {
		super(AttributeType.Exceptions, nameIndex, length);
	}

	/**
	 * returns constant-pool entry indexes of exception classes.
	 * @return constant-pool entry indexes of exception classes
	 */
	public int[] getExceptionIndexTable() {
		return exceptionIndexTable;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.exceptionIndexTable = new int[raf.readShort()];
		for(int i = 0; i < exceptionIndexTable.length; i++) {
			exceptionIndexTable[i] = raf.readShort();
		}
	}
}
