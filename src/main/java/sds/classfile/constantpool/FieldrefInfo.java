package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Fieldref_Info</a>.
 * @author inagaki
 */
public class FieldrefInfo extends MemberRefInfo {
	/**
	 * constructor.
	 */
	public FieldrefInfo() {
		super(ConstantType.C_FIELDREF);
	}
}