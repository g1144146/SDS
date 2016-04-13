package sds.classfile.constantpool;

/**
 * 
 * @author inagaki
 */
public class IntegerInfo extends NumberInfo {
	public IntegerInfo() {
		super(ConstantType.C_INTEGER);
	}

	public int getValue() {
		return number.intValue();
	}
}