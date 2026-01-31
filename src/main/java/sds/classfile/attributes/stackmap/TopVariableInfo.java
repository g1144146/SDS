package sds.classfile.attributes.stackmap;

/**
 * This class is for Top_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class TopVariableInfo extends AbstractVerificationTypeInfo {
	/**
	 * Constructs a TopVariableInfo with the specified tag.
	 * @param tag verification type tag
	 */
	TopVariableInfo(int tag) {
		super(tag, VerificationInfoType.TopVariableInfo);
	}
}
