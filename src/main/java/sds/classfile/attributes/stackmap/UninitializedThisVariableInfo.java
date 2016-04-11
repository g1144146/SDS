package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class UninitializedThisVariableInfo extends AbstractVerificationTypeInfo {
	public UninitializedThisVariableInfo(int tag) {
		super(tag, VerificationInfoType.UninitializedThisVariableInfo);
	}
}
