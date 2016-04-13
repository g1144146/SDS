package sds.classfile.constantpool;

/**
 * 
 * @author inagaki
 */
public class FloatInfo extends NumberInfo {
	public FloatInfo() {
		super(ConstantType.C_FLOAT);
	}

	public float getValue() {
		return number.floatValue();
	}
}