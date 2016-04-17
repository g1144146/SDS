package sds.classfile.attributes.stackmap;

/**
 * This class is for Long_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class LongVariableInfo extends AbstractVerificationTypeInfo {
	LongVariableInfo(int tag) {
		super(tag, VerificationInfoType.LongVariableInfo);
	}
}