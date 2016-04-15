package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6">
 * Constant_NameAndType_Info</a>.
 * @author inagaki
 */
public class NameAndTypeInfo extends ConstantInfo {
	private int nameIndex;
	private int descriptorIndex;

	/**
	 * constructor.
	 */
	public NameAndTypeInfo() {
		super(ConstantType.C_NAME_AND_TYPE);
	}

	/**
	 * returns constant-pool entry index of name
	 * @return constant-pool entry index of name
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * returns constant-pool entry index of descriptor.
	 * @return constant-pool entry index of descriptor
	 */
	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.nameIndex = raf.readShort();
		this.descriptorIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#")
			.append(nameIndex).append(":#").append(descriptorIndex);
		return sb.toString();
	}
}