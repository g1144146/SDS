package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
 * Constant_Double_Info</a>.
 * @author inagaki
 */
public class DoubleInfo extends NumberInfo {
	/**
	 * constructor.
	 */
	public DoubleInfo() {
		super(ConstantType.C_DOUBLE);
	}

	/**
	 * returns double value.
	 * @return value
	 */
	public double getValue() {
		return number.doubleValue();
	}
}