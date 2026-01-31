package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

import static sds.util.AnnotationParser.parseAnnotation;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeVisibleParameterAnnotations <code>RuntimeVisibleParameterAnnotations</code>}
 * and 
 * {@link RuntimeInvisibleParameterAnnotations <code>RuntimeInvisibleParameterAnnotations</code>}
 * have item.
 * @author inagaki
 */
public class ParameterAnnotations {
	private String[] annotations;

	/**
	 * Constructs a ParameterAnnotations from the classfile stream.
	 * @param data classfile stream
	 * @param pool constant-pool
	 * @throws IOException if an error occurs during reading
	 * @throws ElementValueException if an invalid element value is encountered
	 */
	ParameterAnnotations(ClassFileStream data, ConstantPool pool)
	throws IOException, ElementValueException {
		this.annotations = new String[data.readShort()];
		for(int i = 0; i < annotations.length; i++) {
			annotations[i] = parseAnnotation(new Annotation(data), new StringBuilder(), pool);
		}
	}

	/**
	 * returns runtime parameter annotations.
	 * @return runtime parameter annotations
	 */
	public String[] getAnnotations() {
		return annotations;
	}
}
