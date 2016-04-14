package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for array as the value of this element-value.
 * @author inagaki
 */
public class ArrayValue {
	/**
	 * array of element.
	 */
	ElementValue[] values;

	/**
	 * constructor
	 * @param raf classfile stream
	 * @throws IOException
	 */
	ArrayValue(RandomAccessFile raf) throws IOException, ElementValueException {
		this.values = new ElementValue[raf.readShort()];
		for(int i = 0; i < values.length; i++) {
			values[i] = new ElementValue(raf);
		}
	}

	/**
	 * returns array of element.
	 * @return array of element
	 */
	public ElementValue[] getValues() {
		return values;
	}
}