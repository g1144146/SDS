package sds.classfile.attributes.stackmap;

/**
 * This class is for Long_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class LongVariableInfo extends AbstractVerificationTypeInfo {
	/**
	 * Constructs a LongVariableInfo with the specified tag.
	 * @param tag verification type tag
	 */
	LongVariableInfo(int tag) {
		super(tag, VerificationInfoType.LongVariableInfo);
	}
}
