package sds.classfile.attributes.annotation;

/**
 * This enum class is for type of {@link TargetInfo <code>TargetInfo</code>}.
 * @author inagaki
 */
public enum TargetInfoType {
	TypeParameterTarget,
	SuperTypeTarget,
	TypeParameterBoundTarget,
	EmptyTarget,
	MethodFormalParameterTarget,
	ThrowsTarget,
	LocalVarTarget,
	CatchTarget,
	OffsetTarget,
	TypeArgumentTarget;
}