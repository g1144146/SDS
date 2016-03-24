package sophomore.classfile.attributes.annotation;

import sophomore.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class RuntimeInvisibleTypeAnnotations extends RuntimeTypeAnnotations {
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeInvisibleTypeAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeInvisibleTypeAnnotations, nameIndex, length);
	}
}
