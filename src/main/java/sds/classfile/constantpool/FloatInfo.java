package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
 * Constant_Float_Info</a>.
 * @author inagaki
 */
public class FloatInfo extends NumberInfo {
	/**
	 * constructor.
	 */
	public FloatInfo() {
		super(ConstantType.C_FLOAT);
	}

	/**
	 * returns float value.
	 * @return value
	 */
	public float getValue() {
		return number.floatValue();
	}
}