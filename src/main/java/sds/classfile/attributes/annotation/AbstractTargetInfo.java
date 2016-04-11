package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class AbstractTargetInfo implements TargetInfo {
	/**
	 * 
	 */
	TargetInfoType type;

	AbstractTargetInfo(TargetInfoType type) {
		this.type = type;
	}


	@Override
	public TargetInfoType getType() {
		return type;
	}
	
}
