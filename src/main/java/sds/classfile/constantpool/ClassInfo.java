package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1">
 * Constant_Class_Info</a>.
 * @author inagaki
 */
public class ClassInfo extends ConstantInfo {
	/**
	 * constant-pool entry index of class name.
	 */
	int nameIndex;

	/**
	 * constructor.
	 */
	public ClassInfo() {
		super(ConstantType.C_CLASS);
	}

	/**
	 * returns constant entry index of class name.
	 * @return constant entry index of class name
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.nameIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(nameIndex);
		return sb.toString();
	}
}