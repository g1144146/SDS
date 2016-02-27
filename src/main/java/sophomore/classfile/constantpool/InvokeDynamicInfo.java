package sophomore.classfile.constantpool;

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
		super(ConstantType.C_InvokeDynamic);
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
}