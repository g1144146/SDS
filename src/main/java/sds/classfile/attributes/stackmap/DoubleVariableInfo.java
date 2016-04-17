package sds.classfile.attributes.stackmap;

/**
 * This class is for Double_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class DoubleVariableInfo extends AbstractVerificationTypeInfo {
	DoubleVariableInfo(int tag) {
		super(tag, VerificationInfoType.DoubleVariableInfo);
	}
}