package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.9">
 * Constant_MethodType_Info</a>.
 * @author inagaki
 */
public class MethodTypeInfo extends ConstantInfo {
	private int descriptorIndex;

	/**
	 * constructor.
	 */
	public MethodTypeInfo() {
		super(ConstantType.C_METHOD_TYPE);
	}

	/**
	 * returns constant-pool entry index of descriptor of method type.
	 * @return constant-pool entry index of descriptor of method type
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
		sb.append(super.toString()).append("\t#").append(descriptorIndex);
		return sb.toString();
	}
}