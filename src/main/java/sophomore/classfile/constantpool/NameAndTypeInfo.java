package sophomore.classfile.constantpool;

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
		super(ConstantType.C_NameAndType);
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}