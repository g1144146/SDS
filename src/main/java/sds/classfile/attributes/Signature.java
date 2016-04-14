package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9">Signature Attribute</a>.
 * @author inagaki
 */
public class Signature extends AttributeInfo {
	/**
	 * constant-pool entry index of signature.
	 */
	int signatureIndex;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public Signature(int nameIndex, int length) {
		super(AttributeType.Signature, nameIndex, length);
	}

	/**
	 * returns constant-pool entry index of signature.
	 * @return constant-pool entry index of signature
	 */
	public int getSignatureIndex() {
		return signatureIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.signatureIndex = raf.readShort();
	}
}
