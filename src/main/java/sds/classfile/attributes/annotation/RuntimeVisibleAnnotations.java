package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagakikenichi
 */
public class RuntimeVisibleAnnotations extends RuntimeAnnotations {
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeVisibleAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeVisibleAnnotations, nameIndex, length);
	}
}