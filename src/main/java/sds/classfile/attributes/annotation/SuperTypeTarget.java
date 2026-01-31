package sds.classfile.attributes.annotation;

/**
 * This class is for super_type_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class SuperTypeTarget extends AbstractTargetInfo {
	private int superTypeIndex;

	/**
	 * Constructs a SuperTypeTarget with the specified super type index.
	 * @param superTypeIndex index of annotation in extends or implements clause
	 */
	SuperTypeTarget(int superTypeIndex) {
		super(TargetInfoType.SuperTypeTarget);
		this.superTypeIndex = superTypeIndex;
	}

	/**
	 * returns index of annotation.<br>
	 * the index of annotation appears on a typein the extends
	 * or implements clause of a class or interface declaration.
	 * @return index of annotation
	 */
	public int getIndex() {
		return superTypeIndex;
	}
}
