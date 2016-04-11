package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class RuntimeInvisibleParameterAnnotations extends RuntimeParameterAnnotations {
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeInvisibleParameterAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeInvisibleParameterAnnotations, nameIndex, length);
	}
}
