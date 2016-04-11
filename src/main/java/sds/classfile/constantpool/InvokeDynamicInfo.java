package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public class InvokeDynamicInfo extends ConstantInfo {
	/**
	 * 
	 */
	int bootstrapMethodAttrIndex;
	/**
	 * 
	 */
	int nameAndTypeIndex;

	/**
	 * 
	 */
	public InvokeDynamicInfo() {
		super(ConstantType.C_INVOKE_DYNAMIC);
	}

	/**
	 * 
	 * @return 
	 */
	public int getBMAI() {
		return bootstrapMethodAttrIndex;
	}

	/**
	 * 
	 * @return 
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
		String sep = System.getProperty("line.separator");
		sb.append(super.toString()).append(sep).append("\t")
			.append("bootstrap method attribute index: ").append(bootstrapMethodAttrIndex)
			.append(sep).append("\t")
			.append("name and type index             : ").append(nameAndTypeIndex);
		return sb.toString();
	}
}