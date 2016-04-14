package sds.classfile.attributes.annotation;

/**
 * This adapter class is for {@link TargetInfo <code>TargetInfo</code>}.
 * @author inagaki
 */
public class AbstractTargetInfo implements TargetInfo {
	/**
	 * target info type.
	 */
	TargetInfoType type;

	/**
	 * constructor.
	 * @param type target info type
	 */
	AbstractTargetInfo(TargetInfoType type) {
		this.type = type;
	}


	@Override
	public TargetInfoType getType() {
		return type;
	}
	
}
