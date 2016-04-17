package sds.classfile.attributes.stackmap;

/**
 * This adapter class is for
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public abstract class AbstractVerificationTypeInfo implements VerificationTypeInfo {
	private int tag;
	private VerificationInfoType type;

	AbstractVerificationTypeInfo(int tag, VerificationInfoType type) {
		this.tag = tag;
		this.type = type;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public VerificationInfoType getType() {
		return type;
	}
}