package sophomore.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class NullVariableInfo extends AbstractVerificationTypeInfo {
	NullVariableInfo(int tag) {
		super(tag, VerificationInfoType.NullVariableInfo);
	}
}
