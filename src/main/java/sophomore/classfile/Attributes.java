package sophomore.classfile;

import sophomore.classfile.attributes.AttributeInfo;

/**
 * 
 * @author inagaki
 */
public class Attributes implements ArrayInfo<AttributeInfo> {
	/**
	 * 
	 */
	int attrNameIndex;
	/**
	 * 
	 */
	AttributeInfo[] info;

	/**
	 * 
	 * @param size
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