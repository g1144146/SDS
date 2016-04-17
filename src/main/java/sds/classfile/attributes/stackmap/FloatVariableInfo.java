package sds.classfile.attributes.stackmap;

/**
 * This class is for Float_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class FloatVariableInfo extends AbstractVerificationTypeInfo {
	FloatVariableInfo(int tag) {
		super(tag, VerificationInfoType.FloatVariableInfo);
	}
}