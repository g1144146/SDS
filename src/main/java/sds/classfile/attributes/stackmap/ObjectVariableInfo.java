package sds.classfile.attributes.stackmap;

/**
 * This class is for Object_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class ObjectVariableInfo extends AbstractVerificationTypeInfo {
	private int cpoolIndex;

	/**
	 * Constructs an ObjectVariableInfo with the specified tag and constant-pool index.
	 * @param tag verification type tag
	 * @param cpoolIndex constant-pool entry index
	 */
	ObjectVariableInfo(int tag, int cpoolIndex) {
		super(tag, VerificationInfoType.ObjectVariableInfo);
		this.cpoolIndex = cpoolIndex;
	}

	/**
	 * returns constant-pool entry index.
	 * @return constant-pool entry index
	 */
	public int getCPoolIndex() {
		return cpoolIndex;
	}
}
