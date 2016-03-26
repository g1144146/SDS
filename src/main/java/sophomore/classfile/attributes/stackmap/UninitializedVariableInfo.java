package sophomore.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class UninitializedVariableInfo extends AbstractVerificationTypeInfo {
	/**
	 * 
	 */
	int offset;

	UninitializedVariableInfo(int tag, int offset) {
		super(tag, VerificationInfoType.UninitializedVariableInfo);
		this.offset = offset;
	}
}
