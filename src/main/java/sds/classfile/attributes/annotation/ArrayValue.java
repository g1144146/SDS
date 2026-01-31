package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for array as the value of this element-value.
 * @author inagaki
 */
public class ArrayValue {
	private ElementValue[] values;

	/**
	 * Constructs an ArrayValue from the classfile stream.
	 * @param data classfile stream
	 * @throws IOException if an error occurs during reading
	 * @throws ElementValueException if an invalid element value is encountered
	 */
	ArrayValue(ClassFileStream data) throws IOException, ElementValueException {
		this.values = new ElementValue[data.readShort()];
		for(int i = 0; i < values.length; i++) {
			values[i] = new ElementValue(data);
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
