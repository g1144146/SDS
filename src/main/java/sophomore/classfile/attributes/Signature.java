package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Signature extends AttributeInfo {
	/**
	 * 
	 */
	int signatureIndex;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public Signature(int nameIndex, int length) {
		super(AttributeType.Signature, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public int getSignatureIndex() {
		return signatureIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.signatureIndex = raf.readShort();
	}
}
