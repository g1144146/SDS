package sds.classfile.constantpool;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1">
 * Constant_Class_Info</a>.
 * @author inagaki
 */
public class ClassInfo extends ConstantInfo {
	private int nameIndex;

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
	public void read(ClassFileStream data) throws IOException {
		this.nameIndex = data.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(nameIndex);
		return sb.toString();
	}
}