package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This adapter class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Fieldref_Info</a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Methodref_Info</a> and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_InterfaceMethodref_Info</a>.
 * @author inagaki
 */
public abstract class MemberRefInfo extends ConstantInfo {
	/**
	 * constant-pool entry index of class has member.
	 */
	int classIndex;
	/**
	 * constant-pool entry index of member's name and type.
	 */
	int nameAndTypeIndex;

	/**
	 * constructor.
	 * @param tag constant info tag
	 */
	public MemberRefInfo(int tag) {
		super(tag);
	}

	/**
	 * returns constant-pool entry index of class has member.
	 * @return constant-pool entry index of class has member
	 */
	public int getClassIndex() {
		return classIndex;
	}

	/**
	 * returns constant-pool entry index of member's name and type.
	 * @return constant-pool entry index of member's name and type
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