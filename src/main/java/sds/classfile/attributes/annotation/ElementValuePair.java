package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class ElementValuePair {
	/**
	 *
	 */
	int elementNameIndex;
	/**
	 *
	 */
	ElementValue value;
	
	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	ElementValuePair(RandomAccessFile raf) throws IOException, ElementValueException {
		this.elementNameIndex = raf.readShort();
		this.value = new ElementValue(raf);
	}
	
	/**
	 *
	 * @return
	 */
	public int getElementNameIndex() {
		return elementNameIndex;
	}
	
	/**
	 *
	 * @return
	 */
	public ElementValue getValue() {
		return value;
	}
}