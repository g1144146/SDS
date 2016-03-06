package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
class ArrayValue {
	/**
	 *
	 */
	ElementValue[] values;
	
	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	ArrayValue(RandomAccessFile raf) throws IOException {
		int num = raf.readShort();
		this.values = new ElementValue[num];
		for(int i = 0; i < values.length; i++) {
			values[i] = new ElementValue(raf);
		}
	}
	public ElementValue[] getValues() {
		return values;
	}
}