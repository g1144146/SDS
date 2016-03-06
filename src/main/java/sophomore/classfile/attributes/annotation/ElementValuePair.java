package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
class ElementValuePair {
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
	ElementValuePair(RandomAccessFile raf) throws IOException {
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