package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.5">
 * Exceptions Attribute</a>.
 * @author inagaki
 */
public class Exceptions extends AttributeInfo {
	private String[] exceptionTable;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public Exceptions(int nameIndex, int length) {
		super(AttributeType.Exceptions, nameIndex, length);
	}

	/**
	 * returns exception classes.
	 * @return exception classes
	 */
	public String[] getExceptionTable() {
		return exceptionTable;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.exceptionTable = new String[data.readShort()];
		for(int i = 0; i < exceptionTable.length; i++) {
			exceptionTable[i] = extract(pool.get(data.readShort()-1), pool).replace("/", ".");
		}
	}
}