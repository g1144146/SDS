package sds.classfile.attributes.annotation;

/**
 * This class is for throws_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class ThrowsTarget extends AbstractTargetInfo {
	private int throwsTypeIndex;

	ThrowsTarget(int throwsTypeIndex) {
		super(TargetInfoType.ThrowsTarget);
		this.throwsTypeIndex = throwsTypeIndex;
	}

	/**
	 * return index into the exception_index_table array.<br>
	 * the index into the exception_index_table array of the
	 * {@link sds.classfile.attributes.Exceptions
	 * <code>Exceptions attribute</code>}
	 * of the method_info structure enclosing the
	 * {@link RuntimeVisibleTypeAnnotations
	 * <code>RuntimeVisibleTypeAnnotations attribute</code>}.
	 * @return index into the exception_index_table array
	 */
	public int getIndex() {
		return throwsTypeIndex;
	}
}
