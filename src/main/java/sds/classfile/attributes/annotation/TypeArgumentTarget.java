package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class TypeArgumentTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int offset;
	/**
	 * 
	 */
	int typeArgumentIndex;

	TypeArgumentTarget(int offset, int index) {
		super(TargetInfoType.TypeArgumentTarget);
		this.offset = offset;
		this.typeArgumentIndex = index;
	}

	public int getOffset() {
		return offset;
	}

	/**
	 * returns type-argument-index.
	 * @return 
	 */
	public int getIndex() {
		return typeArgumentIndex;
	}
}
