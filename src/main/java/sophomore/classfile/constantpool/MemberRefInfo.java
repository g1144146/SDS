package sophomore.classfile.constantpool;

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
		String sep = System.getProperty("line.separator");
		sb.append(super.toString()).append(sep).append("\t")
			.append("class index        : ").append(classIndex).append(sep).append("\t")
			.append("name and type index: ").append(nameAndTypeIndex);
		return sb.toString();
	}
}