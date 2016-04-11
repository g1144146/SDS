package sds.classfile;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class MemberInfo implements Info {
	/**
	 * 
	 */
	int accessFlags;
	/**
	 * 
	 */
	int nameIndex;
	/**
	 * 
	 */
	int descriptorIndex;
	/**
	 * 
	 */
	Attributes attr;
	/**
	 * 
	 */
	private String type;

	/**
	 * 
	 * @param type 
	 */
	MemberInfo(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return 
	 */
	public int getAccessFlags() {
		return accessFlags;
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

	/**
	 * 
	 * @return 
	 */
	public Attributes getAttr() {
		return attr;
	}

	/**
	 * 
	 * @param attr 
	 */
	public void setAttr(Attributes attr) {
		this.attr = attr;
	}

	/**
	 * 
	 * @return 
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