package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class OffsetTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	int offset;

	OffsetTarget(int offset) {
		super(TargetInfoType.OffsetTarget);
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}
}
