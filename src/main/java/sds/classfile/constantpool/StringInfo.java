package sds.classfile.constantpool;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.3">
 * Constant_String_Info</a>.
 * @author inagaki
 */
public class StringInfo extends ConstantInfo {
	private int stringIndex;

	/**
	 * constructor.
	 */
	public StringInfo() {
		super(ConstantType.C_STRING);
	}

	/**
	 * returns constant-pool entry index of string.
	 * @return constant-pool entry index of string
	 */
	public int getStringIndex() {
		return stringIndex;
	}

	@Override
	public void read(ClassFileStream data) throws IOException {
		this.stringIndex = data.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(stringIndex);
		return sb.toString();
	}
}