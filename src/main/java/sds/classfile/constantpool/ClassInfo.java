package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1">
 * Constant_Class_Info</a>.
 * @author inagaki
 */
public class ClassInfo extends ConstantInfo {
	private int nameIndex;

	/**
     * constructor.
     * @param nameIndex constant entry index of class name
     */
	public ClassInfo(int nameIndex) {
		super(ConstantType.C_CLASS);
        this.nameIndex = nameIndex;
	}

	/**
	 * returns constant entry index of class name.
	 * @return constant entry index of class name
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	@Override
	public String toString() {
		return super.toString() + "\t#" + nameIndex;
	}
}