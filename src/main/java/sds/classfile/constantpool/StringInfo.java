package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public class StringInfo extends ConstantInfo {
	/**
	 * 
	 */
	int stringIndex;

	/**
	 * 
	 */
	public StringInfo() {
		super(ConstantType.C_STRING);
	}

	/**
	 * 
	 * @return 
	 */
	public int getStringIndex() {
		return stringIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.stringIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(stringIndex);
		return sb.toString();
	}
}