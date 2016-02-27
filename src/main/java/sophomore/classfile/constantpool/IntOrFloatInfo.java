package sophomore.classfile.constantpool;

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
}