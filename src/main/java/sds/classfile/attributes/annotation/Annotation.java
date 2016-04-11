package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Annotation {
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
	Annotation(RandomAccessFile raf) throws IOException, ElementValueException {
		this.typeIndex = raf.readShort();
		this.elementValuePairs = new ElementValuePair[raf.readShort()];
		for(int i = 0; i < elementValuePairs.length; i++) {
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