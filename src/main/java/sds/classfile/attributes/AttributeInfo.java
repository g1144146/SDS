package sds.classfile.attributes;

import sds.classfile.Info;

/**
 * This adapter class is for info of class has attribute.
 * @author inagaki
 */
public abstract class AttributeInfo implements Info {
	private int nameIndex;
	private int attrLen;
	private AttributeType type;

	/**
	 * constructor.
	 * @param type attribute type
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public AttributeInfo(AttributeType type, int nameIndex, int length) {
		this.type = type;
		this.nameIndex = nameIndex;
		this.attrLen = length;
	}

	/**
	 * returns constant-pool entry index of attribute name.
	 * @return constant-pool entry index of attribute name
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * returns attribute length.
	 * @return length
	 */
	public int getAttrLen() {
		return attrLen;
	}

	/**
	 * returns attribute type.
	 * @return type
	 */
	public AttributeType getType() {
		return type;
	}
}