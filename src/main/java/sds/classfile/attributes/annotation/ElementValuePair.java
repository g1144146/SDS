package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for element-value pair in the annotation.
 * @author inagaki
 */
public class ElementValuePair {
	private int elementNameIndex;
	private ElementValue value;

	ElementValuePair(RandomAccessFile raf) throws IOException, ElementValueException {
		this.elementNameIndex = raf.readShort();
		this.value = new ElementValue(raf);
	}
	
	/**
	 * returns constant-pool entry index of element name.
	 * @return constant-pool entry index of element name
	 */
	public int getElementNameIndex() {
		return elementNameIndex;
	}
	
	/**
	 * returns single element-value in the annotation.
	 * @return single element-value
	 */
	public ElementValue getValue() {
		return value;
	}
}