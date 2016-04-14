package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.7">EnclosingMethod Attribute</a>.
 * @author inagaki
 */
public class EnclosingMethod extends AttributeInfo {
	/**
	 * constant-pool entry index of class has this method.
	 */
	int classIndex;
	/**
	 * constant-pool entry index of this method.
	 */
	int methodIndex;
	
	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public EnclosingMethod(int nameIndex, int length) {
		super(AttributeType.EnclosingMethod, nameIndex, length);
	}

	/**
	 * returns constant-pool entry index of class has this method.
	 * @return constant-pool entry index of class has this method
	 */
	public int classIndex() {
		return classIndex;
	}

	/**
	 * returns constant-pool entry index of this method.
	 * @return constant-pool entry index of this method
	 */
	public int methodIndex() {
		return methodIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.classIndex  = raf.readShort();
		this.methodIndex = raf.readShort();
	}
}
