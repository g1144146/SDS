package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.18">RuntimeVisibleParameterAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeVisibleParameterAnnotations extends RuntimeParameterAnnotations {
	/**
	  * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public RuntimeVisibleParameterAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeVisibleParameterAnnotations, nameIndex, length);
	}
}