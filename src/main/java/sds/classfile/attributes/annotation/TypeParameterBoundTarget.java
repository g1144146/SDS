package sds.classfile.attributes.annotation;

/**
 * This class is for type_parameter_bound_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagakikenichi
 */
public class TypeParameterBoundTarget extends AbstractTargetInfo {
	private int typeParameterIndex;
	private int boundIndex;

	TypeParameterBoundTarget(int typeParameterIndex, int boundIndex) {
		super(TargetInfoType.TypeParameterBoundTarget);
		this.typeParameterIndex = typeParameterIndex;
		this.boundIndex = boundIndex;
	}

	/**
	 * returns index of type parameter declaration has an annotated bound.
	 * @return index of type parameter declaration
	 */
	public int getTPI() {
		return typeParameterIndex;
	}

	/**
	 * returns index of bound of the type parameter declaration.<br>
	 * the index of bound of the type parameter declaration indicated
	 * by type_parameter_index is annotated.
	 * @return index of bound of the type parameter declaration
	 */
	public int getBoundIndex() {
		return boundIndex;
	}
}
