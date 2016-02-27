package sophomore.classfile.constantpool;

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
}