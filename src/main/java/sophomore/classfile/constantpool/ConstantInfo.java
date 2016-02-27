package sophomore.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * 
 * @author inagaki
 */
public abstract class ConstantInfo {
	/**
	 * 
	 */
	int tag;

	/**
	 * 
	 * @param tag 
	 */
	public ConstantInfo(int tag) {
		this.tag = tag;
	}

	/**
	 * 
	 * @return 
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	public abstract void read(RandomAccessFile raf) throws IOException;
}