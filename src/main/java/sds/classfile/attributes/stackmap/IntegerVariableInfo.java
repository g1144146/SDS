package sds.classfile.attributes.stackmap;

/**
 * This class is for Integer_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class IntegerVariableInfo extends AbstractVerificationTypeInfo {
	IntegerVariableInfo(int tag) {
		super(tag, VerificationInfoType.IntegerVariableInfo);
	}	
}
