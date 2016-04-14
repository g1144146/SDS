package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
 * Constant_Integer_Info</a>.
 * @author inagaki
 */
public class IntegerInfo extends NumberInfo {
	/**
	 * constructor.
	 */
	public IntegerInfo() {
		super(ConstantType.C_INTEGER);
	}

	/**
	 * returns int value.
	 * @return value
	 */
	public int getValue() {
		return number.intValue();
	}
}