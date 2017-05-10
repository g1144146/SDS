package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10">
 * Constant_InvokeDynamic_Info</a>.
 * @author inagaki
 */
public class InvokeDynamicInfo extends ConstantInfo {
	private int bsmAttrIndex;
	private int nameAndTypeIndex;

    /**
     * constructor.
     * @param bsmAttrIndex entry index of bootstrap method attribute
     * @param nameAndTypeIndex constant-pool entry index of name and type
     */
	public InvokeDynamicInfo(int bsmAttrIndex, int nameAndTypeIndex) {
		super(ConstantType.C_INVOKE_DYNAMIC);
        this.bsmAttrIndex = bsmAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
	}

	/**
	 * returns entry index of bootstrap method attribute.
	 * @return entry index of bootstrap method attribute
	 */
	public int getBMAIndex() {
		return bsmAttrIndex;
	}

	/**
	 * returns constant-pool entry index of name and type.
	 * @return constant-pool entry index of name and type
	 */
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(bsmAttrIndex)
			.append(":#").append(nameAndTypeIndex);
		return sb.toString();
	}
}