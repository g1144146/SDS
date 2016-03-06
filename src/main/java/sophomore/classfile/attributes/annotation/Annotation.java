package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class Annotation {
	/**
	 *
	 */
	int typeIndex;
	/**
	 *
	 */
	ElementValuePair[] elementValuePairs;
	
	Annotation() {}
	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	Annotation(RandomAccessFile raf) throws IOException {
		this.typeIndex = raf.readShort();
		int len = raf.readShort();
		this.elementValuePairs = new ElementValuePair[len];
		for(int i = 0; i < len; i++) {
			elementValuePairs[i] = new ElementValuePair(raf);
		}
	}
	public int getTypeIndex() {
		return typeIndex;
	}
	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}
}