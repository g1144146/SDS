package sds.classfile.constantpool;

/**
 * This adapter class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Fieldref_Info</a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Methodref_Info</a> and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_InterfaceMethodref_Info</a>.
 * @author inagaki
 */
public class MemberRefInfo extends ConstantInfo {
	private int classIndex;
	private int nameAndTypeIndex;

	/**
     * 
     * @param tag constant info tag
     * @param classIndex constant-pool entry index of class has member
     * @param nameAndTypeIndex constant-pool entry index of member's name and type
     */
	public MemberRefInfo(int tag, int classIndex, int nameAndTypeIndex) {
		super(tag);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
	}

	/**
	 * returns constant-pool entry index of class has member.
	 * @return constant-pool entry index of class has member
	 */
	public int getClassIndex() {
		return classIndex;
	}

	/**
	 * returns constant-pool entry index of member's name and type.
	 * @return constant-pool entry index of member's name and type
	 */
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	@Override
	public String toString() {
		return super.toString() + "\t#" + classIndex + ".#" + nameAndTypeIndex;
	}
}