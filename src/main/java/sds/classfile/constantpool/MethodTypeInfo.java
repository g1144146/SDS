package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public class MethodTypeInfo extends ConstantInfo {
	/**
	 * 
	 */
	int descriptorIndex;

	/**
	 * 
	 */
	public MethodTypeInfo() {
		super(ConstantType.C_METHOD_TYPE);
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
		this.descriptorIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = System.getProperty("line.separator");
		sb.append(super.toString()).append(sep).append("\t")
			.append("descriptor index: ").append(descriptorIndex);
		return sb.toString();
	}
}