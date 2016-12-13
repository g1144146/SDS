package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20">
 * RuntimeVisibleTypeAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeVisibleTypeAnnotations extends RuntimeTypeAnnotations {
	/**
	 * constructor.
	 */
	public RuntimeVisibleTypeAnnotations() {
		super(AttributeType.RuntimeVisibleTypeAnnotations);
	}
}