package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public class NameAndTypeInfo extends ConstantInfo {
	/**
	 * 
	 */
	private int nameIndex;
	/**
	 * 
	 */
	private int descriptorIndex;

	/**
	 * 
	 */
	public NameAndTypeInfo() {
		super(ConstantType.C_NAME_AND_TYPE);
	}

	/**
	 * 
	 * @return 
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * 
	 * @return 
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