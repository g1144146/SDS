package sophomore.classfile.attributes.annotation;

import sophomore.classfile.attributes.AttributeType;

/**
 *
 * @author inagakikenichi
 */
public class RuntimeVisibleTypeAnnotations extends RuntimeTypeAnnotations {
	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeVisibleTypeAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeVisibleTypeAnnotations, nameIndex, length);
	}
}
