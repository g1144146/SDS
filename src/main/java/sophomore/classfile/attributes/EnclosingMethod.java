package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class EnclosingMethod extends AttributeInfo {
	/**
	 * 
	 */
	int classIndex;
	/**
	 * 
	 */
	int methodIndex;
	
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public EnclosingMethod(int nameIndex, int length) {
		super(AttributeType.Type.EnclosingMethod, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public int classIndex() {
		return classIndex;
	}

	/**
	 * 
	 * @return 
	 */
	public int methodIndex() {
		return methodIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.classIndex  = raf.readShort();
		this.methodIndex = raf.readShort();
	}
	
}
