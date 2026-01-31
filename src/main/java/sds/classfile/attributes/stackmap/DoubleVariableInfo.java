package sds.classfile.attributes.stackmap;

/**
 * This class is for Double_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class DoubleVariableInfo extends AbstractVerificationTypeInfo {
	/**
	 * Constructs a DoubleVariableInfo with the specified tag.
	 * @param tag verification type tag
	 */
	DoubleVariableInfo(int tag) {
		super(tag, VerificationInfoType.DoubleVariableInfo);
	}
}
