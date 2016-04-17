package sds.classfile.attributes.stackmap;

/**
 * This class is for Uninitialized_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class UninitializedVariableInfo extends AbstractVerificationTypeInfo {
	private int offset;

	UninitializedVariableInfo(int tag, int offset) {
		super(tag, VerificationInfoType.UninitializedVariableInfo);
		this.offset = offset;
	}

	/**
	 * returns offset.
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}
}