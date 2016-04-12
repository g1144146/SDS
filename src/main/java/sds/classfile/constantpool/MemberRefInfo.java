package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public abstract class MemberRefInfo extends ConstantInfo {
	/**
	 * 
	 */
	int classIndex;
	/**
	 * 
	 */
	int nameAndTypeIndex;

	/**
	 * 
	 * @param tag 
	 */
	public MemberRefInfo(int tag) {
		super(tag);
	}

	/**
	 * 
	 * @return 
	 */
	public int getClassIndex() {
		return classIndex;
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
		this.classIndex = raf.readShort();
		this.nameAndTypeIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#")
			.append(classIndex).append(".#").append(nameAndTypeIndex);
		return sb.toString();
	}
}