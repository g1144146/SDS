package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Methodref_Info</a>.
 * @author inagaki
 */
public class MethodrefInfo extends MemberRefInfo {
	/**
	 * constructor.
	 */
	public MethodrefInfo() {
		super(ConstantType.C_METHODREF);
	}
}