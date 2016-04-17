package sds.classfile.attributes.stackmap;

/**
 * This class is for UninitializedThis_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class UninitializedThisVariableInfo extends AbstractVerificationTypeInfo {
	UninitializedThisVariableInfo(int tag) {
		super(tag, VerificationInfoType.UninitializedThisVariableInfo);
	}
}