package sophomore.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public enum TargetInfoType {
	TypeParameterTarget("type_parameter"),
	SuperTypeTarget("super_type"),
	TypeParameterBoundTarget("type_parameter_bound"),
	EmptyTarget("empty_target"),
	MethodFormalParameterTarget("method_formal_parameter"),
	ThrowsTarget("throws_target"),
	LocalVarTarget("local_var"),
	CatchTarget("catch"),
	OffsetTarget("offset"),
	TypeArgumentTarget("type_argument");

	/**
	 * 
	 */
	String type;

	TargetInfoType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
}
