package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class SuperTypeTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int superTypeIndex;

	SuperTypeTarget(int superTypeIndex) {
		super(TargetInfoType.SuperTypeTarget);
	}

	/**
	 * returns super-type-index.
	 * @return 
	 */
	public int getIndex() {
		return superTypeIndex;
	}
}
