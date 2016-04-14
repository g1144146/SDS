package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7">
 * Constant_Utf8_Info</a>.
 * @author inagaki
 */
public class Utf8Info extends ConstantInfo {
	/**
	 * string length.
	 */
	int length;
	/**
	 * string of this constant info has.
	 */
	String value;
	
	/**
	 * constructor.
	 */
	public Utf8Info() {
		super(ConstantType.C_UTF8);
	}

	/**
	 * returns string length.
	 * @return string length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * returns string of this constant info has.
	 * @return string
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.length = raf.readShort();
		byte[] b = new byte[length];
		raf.readFully(b);
		this.value = new String(b, "UTF-8");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t").append(value);
		return sb.toString();
	}
}