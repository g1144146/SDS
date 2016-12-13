package sds.classfile.attributes.annotation;

import sds.classfile.attributes.AttributeType;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.19">RuntimeInvisibleParameterAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeInvisibleParameterAnnotations extends RuntimeParameterAnnotations {
	/**
	 * constructor.
	 */
	public RuntimeInvisibleParameterAnnotations() {
		super(AttributeType.RuntimeInvisibleParameterAnnotations);
	}
}