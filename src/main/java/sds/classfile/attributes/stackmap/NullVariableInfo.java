package sds.classfile.attributes.stackmap;

/**
 * This class is for Null_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class NullVariableInfo extends AbstractVerificationTypeInfo {
	NullVariableInfo(int tag) {
		super(tag, VerificationInfoType.NullVariableInfo);
	}
}