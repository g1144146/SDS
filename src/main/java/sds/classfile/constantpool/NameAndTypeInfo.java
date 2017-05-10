package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6">
 * Constant_NameAndType_Info</a>.
 * @author inagaki
 */
public class NameAndTypeInfo extends ConstantInfo {
	private int nameIndex;
	private int descIndex;

	/**
     * constructor.
     * @param nameIndex constant-pool entry index of name
     * @param descIndex  constant-pool entry index of descriptor
     */
	public NameAndTypeInfo(int nameIndex, int descIndex) {
		super(ConstantType.C_NAME_AND_TYPE);
        this.nameIndex = nameIndex;
        this.descIndex = descIndex;
	}

	/**
	 * returns constant-pool entry index of name.
	 * @return constant-pool entry index of name
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * returns constant-pool entry index of descriptor.
	 * @return constant-pool entry index of descriptor
	 */
	public int getDescIndex() {
		return descIndex;
	}

	@Override
	public String toString() {
		return super.toString() + "\t#" + nameIndex + ":#" + descIndex;
	}
}