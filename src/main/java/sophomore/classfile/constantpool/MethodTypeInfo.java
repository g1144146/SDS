package sophomore.classfile.constantpool;

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
		super(ConstantType.C_MethodType);
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
}