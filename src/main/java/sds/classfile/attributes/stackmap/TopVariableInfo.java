package sds.classfile.attributes.stackmap;

/**
 * This class is for Top_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class TopVariableInfo extends AbstractVerificationTypeInfo {
	TopVariableInfo(int tag) {
		super(tag, VerificationInfoType.TopVariableInfo);
	}
}