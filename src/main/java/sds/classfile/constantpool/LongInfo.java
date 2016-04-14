package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
 * Constant_Long_Info</a>.
 * @author inagaki
 */
public class LongInfo extends NumberInfo {
	/**
	 * constructor.
	 */
	public LongInfo() {
		super(ConstantType.C_LONG);
	}

	/**
	 * returns long value.
	 * @return value
	 */
	public long getValue() {
		return number.longValue();
	}
}