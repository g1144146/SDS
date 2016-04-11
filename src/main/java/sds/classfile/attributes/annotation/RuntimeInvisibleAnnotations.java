package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class RuntimeInvisibleAnnotations extends RuntimeAnnotations {

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeInvisibleAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeInvisibleAnnotations, nameIndex, length);
	}
}
