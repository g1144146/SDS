package sds.classfile;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This adapter class is for info of class has member.
 * @author inagaki
 */
public class MemberInfo implements Info {
	private int accessFlags;
	private int nameIndex;
	private int descriptorIndex;
	private Attributes attr;
	private String type;

	/**
	 * constructor.
	 * @param type type of member.
	 */
	MemberInfo(String type) {
		this.type = type;
	}

	/**
	 * returns access flag of member.
	 * @return access flag
	 */
	public int getAccessFlags() {
		return accessFlags;
	}

	/**
	 * returns constant-pool entry index of member name.
	 * @return constant-pool entry index of member name
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * returns constant-pool entry index of member descriptor.
	 * @return constant-pool entry index of member descriptor
	 */
	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	/**
	 * returns attributes of member.
	 * @return attributes
	 */
	public Attributes getAttr() {
		return attr;
	}

	/**
	 * sets attributes of member.
	 * @param attr attributes
	 */
	public void setAttr(Attributes attr) {
		this.attr = attr;
	}

	/**
	 * returns type of member.
	 * @return type
	 */
	public String getType() {
		return type;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.accessFlags = raf.readShort();
		this.nameIndex = raf.readShort();
		this.descriptorIndex = raf.readShort();
	}
}