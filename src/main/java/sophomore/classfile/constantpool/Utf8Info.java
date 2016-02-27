package sophomore.classfile.constantpool;

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
	int[] bytes;

	/**
	 * 
	 */
	public Utf8Info() {
		super(ConstantType.C_Utf8);
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
	public int[] getBytes() {
		return bytes;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.length = raf.readShort();
		this.bytes = new int[length];
	}
}