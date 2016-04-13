package sds.classfile.constantpool;

/**
 * 
 * @author inagaki
 */
public class DoubleInfo extends NumberInfo {
	public DoubleInfo() {
		super(ConstantType.C_DOUBLE);
	}

	public double getValue() {
		return number.doubleValue();
	}
}