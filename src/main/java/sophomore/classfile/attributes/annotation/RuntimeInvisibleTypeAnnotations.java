package sophomore.classfile.attributes.annotation;

import sophomore.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class RuntimeInvisibleTypeAnnotations extends RuntimeParameterAnnotations {
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeInvisibleTypeAnnotations(int nameIndex, int length) {
		super(AttributeType.Type.RuntimeInvisibleTypeAnnotations, nameIndex, length);
	}
}
