package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9">
 * Signature Attribute</a>.
 * @author inagaki
 */
public class Signature extends AttributeInfo {
	private String signature;

	/**
	 * constructor.
	 */
	public Signature() {
		super(AttributeType.Signature);
	}

	/**
	 * returns signature.
	 * @return signature
	 */
	public String getSignature() {
		return signature;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.signature = extract(pool.get(data.readShort()-1), pool);
	}
}