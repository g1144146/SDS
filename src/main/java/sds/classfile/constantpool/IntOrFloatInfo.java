package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
abstract class IntOrFloatInfo extends ConstantInfo {

	/**
	 * 
	 */
	int bytes;

	/**
	 * 
	 * @param tag 
	 */
	public IntOrFloatInfo(int tag) {
		super(tag);
	}

	/**
	 * 
	 * @return 
	 */
	public int getBytes() {
		return bytes;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.bytes = raf.readInt();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t").append(bytes);
		return sb.toString();
	}
}