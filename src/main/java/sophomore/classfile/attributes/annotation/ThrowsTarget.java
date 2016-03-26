package sophomore.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class ThrowsTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int throwsTypeIndex;

	ThrowsTarget(int throwsTypeIndex) {
		super(TargetInfoType.ThrowsTarget);
		this.throwsTypeIndex = throwsTypeIndex;
	}

	/**
	 * return throws-type-index.
	 * @return 
	 */
	public int getIndex() {
		return throwsTypeIndex;
	}
}
