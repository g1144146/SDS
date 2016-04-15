package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10">
 * Constant_InvokeDynamic_Info</a>.
 * @author inagaki
 */
public class InvokeDynamicInfo extends ConstantInfo {
	private int bootstrapMethodAttrIndex;
	private int nameAndTypeIndex;

	/**
	 * constructor.
	 */
	public InvokeDynamicInfo() {
		super(ConstantType.C_INVOKE_DYNAMIC);
	}

	/**
	 * returns entry index of bootstrap method attribute.
	 * @return entry index of bootstrap method attribute
	 */
	public int getBMAI() {
		return bootstrapMethodAttrIndex;
	}

	/**
	 * returns constant-pool entry index of name and type.
	 * @return constant-pool entry index of name and type
	 */
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.bootstrapMethodAttrIndex = raf.readShort();
		this.nameAndTypeIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(bootstrapMethodAttrIndex)
			.append(":#").append(nameAndTypeIndex);
		return sb.toString();
	}
}