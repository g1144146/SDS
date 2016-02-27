package sophomore.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * 
 * @author inagaki
 */
public class ClassInfo extends ConstantInfo {
	/**
	 * 
	 */
	int nameIndex;

	/**
	 * 
	 */
	public ClassInfo() {
		super(ConstantType.C_Class);
	}

	/**
	 * 
	 * @return 
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.nameIndex = raf.readShort();
	}
}