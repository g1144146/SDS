package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_InterfaceMethodref_Info</a>.
 * @author inagaki
 */
public class InterfaceMethodrefInfo extends MemberRefInfo {
	/**
	 * constructor.
	 */
	public InterfaceMethodrefInfo() {
		super(ConstantType.C_INTERFACE_METHODREF);
	}
}