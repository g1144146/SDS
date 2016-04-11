package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class TopVariableInfo extends AbstractVerificationTypeInfo {
	TopVariableInfo(int tag) {
		super(tag, VerificationInfoType.TopVariableInfo);
	}
}
