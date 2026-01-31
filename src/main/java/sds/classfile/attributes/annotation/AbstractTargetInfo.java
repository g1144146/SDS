package sds.classfile.attributes.annotation;

/**
 * This adapter class is for {@link TargetInfo <code>TargetInfo</code>}.
 * @author inagaki
 */
public class AbstractTargetInfo implements TargetInfo {
	private TargetInfoType type;

	/**
	 * Constructs an AbstractTargetInfo with the specified target info type.
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
