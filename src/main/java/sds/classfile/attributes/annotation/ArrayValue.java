package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class ArrayValue {
	/**
	 *
	 */
	ElementValue[] values;
	
	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	ArrayValue(RandomAccessFile raf) throws IOException, ElementValueException {
		this.values = new ElementValue[raf.readShort()];
		for(int i = 0; i < values.length; i++) {
			values[i] = new ElementValue(raf);
		}
	}
	public ElementValue[] getValues() {
		return values;
	}
}