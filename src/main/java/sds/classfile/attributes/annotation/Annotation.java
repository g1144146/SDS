package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeVisibleAnnotations <code>RuntimeVisibleAnnotations</code>} and 
 * {@link RuntimeInvisibleAnnotations <code>RuntimeInvisibleAnnotations</code>} have item.
 * @author inagaki
 */
public class Annotation {
	int typeIndex;
	ElementValuePair[] elementValuePairs;
	
	/**
	 * Default constructor.
	 */
	Annotation() {}

	/**
	 * Constructs an Annotation from the classfile stream.
	 * @param data classfile stream
	 * @throws IOException if an error occurs during reading
	 * @throws ElementValueException if an invalid element value is encountered
	 */
	Annotation(ClassFileStream data) throws IOException, ElementValueException {
		this.typeIndex = data.readShort();
		this.elementValuePairs = new ElementValuePair[data.readShort()];
		for(int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(data);
		}
	}

	/**
	 * constant-pool entry index of annotation type.
	 * @return constant-pool entry index of annotation type
	 */
	public int getTypeIndex() {
		return typeIndex;
	}

	/**
	 * element-value pair in the annotation.
	 * @return element-value pair
	 */
	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}
}
