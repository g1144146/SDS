package sds.classfile.constantpool;

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
		super(ConstantType.C_CLASS);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(nameIndex);
		return sb.toString();
	}
}