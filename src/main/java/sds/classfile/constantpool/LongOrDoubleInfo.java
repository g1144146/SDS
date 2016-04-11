package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
abstract class LongOrDoubleInfo extends ConstantInfo {
	/**
	 * 
	 */
	int highBytes;
	/**
	 * 
	 */
	int lowBytes;

	/**
	 * 
	 * @param tag 
	 */
	public LongOrDoubleInfo(int tag) {
		super(tag);
	}

	/**
	 * 
	 * @return 
	 */
	public int getHighBytes() {
		return highBytes;
	}

	/**
	 * 
	 * @return 
	 */
	public int getLowBytes() {
		return lowBytes;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.highBytes = raf.readInt();
		this.lowBytes  = raf.readInt();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = System.getProperty("line.separator");
		sb.append(super.toString()).append(sep).append("\t")
			.append("high bytes: ").append(highBytes).append(sep).append("\t")
			.append("low  bytes: ").append(lowBytes);
		return sb.toString();
	}
}