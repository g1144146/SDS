package sds.classfile.attributes.stackmap;

/**
 * This class is for Integer_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class IntegerVariableInfo extends AbstractVerificationTypeInfo {
	/**
	 * Constructs an IntegerVariableInfo with the specified tag.
	 * @param tag verification type tag
	 */
	IntegerVariableInfo(int tag) {
		super(tag, VerificationInfoType.IntegerVariableInfo);
	}
}
