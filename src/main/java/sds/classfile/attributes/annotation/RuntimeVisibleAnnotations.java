package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.16">RuntimeVisibleAnnotations Attribute</a>.
 * @author inagakikenichi
 */
public class RuntimeVisibleAnnotations extends RuntimeAnnotations {
	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public RuntimeVisibleAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeVisibleAnnotations, nameIndex, length);
	}
}