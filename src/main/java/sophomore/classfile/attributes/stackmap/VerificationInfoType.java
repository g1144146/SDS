package sophomore.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public enum VerificationInfoType {
	TopVariableInfo(0, "top"),
	IntegerVariableInfo(1, "integer"),
	FloatVariableInfo(2, "float"),
	DoubleVariableInfo(3, "double"),
	LongVariableInfo(4, "long"),
	NullVariableInfo(5, "null"),
	UninitializedThisVariableInfo(6, "uninitialized_this"),
	ObjectVariableInfo(7, "object"),
	UninitializedVariableInfo(8, "uninitialized");

	/**
	 * 
	 */
	int tag;
	/**
	 * 
	 */
	String type;

	VerificationInfoType(int tag, String type) {
		this.tag = tag;
		this.type = type;
	}

	public int getTag() {
		return tag;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
}
