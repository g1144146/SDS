package sophomore.classfile.attributes;

import java.io.IOException;
//import java.io.ByteArrayInputStream;
//import java.io.DataInputStream;
import java.io.RandomAccessFile;
import sophomore.classfile.Info;

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
	private AttributeType.Type type;

//	DataInputStream dataStream;

	/**
	 * 
	 * @param type
	 * @param nameIndex
	 * @param length 
	 */
	public AttributeInfo(AttributeType.Type type, int nameIndex, int length) {
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
	public AttributeType.Type getType() {
		return type;
	}
}
