package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public class Utf8Info extends ConstantInfo {
	/**
	 * 
	 */
	int length;
	/**
	 * 
	 */
	String value;
	
	/**
	 * 
	 */
	public Utf8Info() {
		super(ConstantType.C_UTF8);
	}

	/**
	 * 
	 * @return 
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 
	 * @return 
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
		String sep = System.getProperty("line.separator");
		sb.append(super.toString()).append(sep).append("\t")
			.append("length : ").append(length).append(sep).append("\t")
			.append("value  : ").append(value);
		return sb.toString();
	}
}