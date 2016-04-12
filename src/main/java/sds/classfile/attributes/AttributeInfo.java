package sds.classfile.attributes;

//import java.io.IOException;
//import java.io.ByteArrayInputStream;
//import java.io.DataInputStream;
//import java.io.RandomAccessFile;
import sds.classfile.Info;

/**
 *
 * @author inagaki
 */
public abstract class AttributeInfo implements Info {
	/**
	 * 
	 */
	int nameIndex;
	/**
	 * 
	 */
	int attrLen;
	/**
	 * 
	 */
	byte[] data;
	/**
	 * 
	 */
	private AttributeType type;

	/**
	 * 
	 * @param type
	 * @param nameIndex
	 * @param length 
	 */
	public AttributeInfo(AttributeType type, int nameIndex, int length) {
		this.type = type;
		this.nameIndex = nameIndex;
		this.attrLen = length;
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
	public int getAttrLen() {
		return attrLen;
	}

	/**
	 * 
	 * @return 
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * 
	 * @return 
	 */
	public AttributeType getType() {
		return type;
	}
}