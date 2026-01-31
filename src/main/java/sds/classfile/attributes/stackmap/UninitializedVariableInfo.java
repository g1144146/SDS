package sds.classfile.attributes.stackmap;

/**
 * This class is for Uninitialized_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class UninitializedVariableInfo extends AbstractVerificationTypeInfo {
	private int offset;

	/**
	 * Constructs an UninitializedVariableInfo with the specified tag and offset.
	 * @param tag verification type tag
	 * @param offset offset of new instruction
	 */
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
