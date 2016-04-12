package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class ObjectVariableInfo extends AbstractVerificationTypeInfo {
	/**
	 * 
	 */
	int cpoolIndex;

	ObjectVariableInfo(int tag, int cpoolIndex) {
		super(tag, VerificationInfoType.ObjectVariableInfo);
		this.cpoolIndex = cpoolIndex;
	}

	public int getCPoolIndex() {
		return cpoolIndex;
	}
}