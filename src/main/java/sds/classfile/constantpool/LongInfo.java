package sds.classfile.constantpool;

/**
 * 
 * @author inagaki
 */
public class LongInfo extends NumberInfo {
	public LongInfo() {
		super(ConstantType.C_LONG);
	}

	public long getValue() {
		return number.longValue();
	}
}