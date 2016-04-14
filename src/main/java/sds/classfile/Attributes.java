package sds.classfile;

import sds.classfile.attributes.AttributeInfo;

/**
 * This class is for attributes of class.
 * @author inagaki
 */
public class Attributes implements ArrayInfo<AttributeInfo> {
	/**
	 * array of attributes.
	 */
	AttributeInfo[] info;

	/**
	 * constructor.
	 * @param size size of array
	 */
	public Attributes(int size) {
		this.info = new AttributeInfo[size];
	}

	@Override
	public int size() {
		return info.length;
	}

	@Override
	public void add(int index, AttributeInfo element) {
		if(index >= info.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		info[index] = element;
	}

	@Override
	public AttributeInfo get(int index) {
		if(index >= info.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return info[index];
	}

	@Override
	public AttributeInfo[] getAll() {
		return info;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}