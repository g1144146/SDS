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
	public int getSize() {
		return info.length;
	}

	@Override
	public void add(int index, AttributeInfo element) {
		if(index >= info.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		info[index] = element;
	}

	@Override
	public AttributeInfo get(int index) {
		if(index >= info.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return info[index];
	}

	@Override
	public AttributeInfo[] getAll() {
		return info;
	}
}