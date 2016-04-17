package sds.classfile.attributes.stackmap;

/**
 * This enum class is for type of
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public enum VerificationInfoType {
	TopVariableInfo,
	IntegerVariableInfo,
	FloatVariableInfo,
	DoubleVariableInfo,
	LongVariableInfo,
	NullVariableInfo,
	UninitializedThisVariableInfo,
	ObjectVariableInfo,
	UninitializedVariableInfo;
}