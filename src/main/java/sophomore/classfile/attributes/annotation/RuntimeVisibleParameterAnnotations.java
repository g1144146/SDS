package sophomore.classfile.attributes.annotation;

import sophomore.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class RuntimeVisibleParameterAnnotations extends RuntimeParameterAnnotations {

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public RuntimeVisibleParameterAnnotations(int nameIndex, int length) {
		super(AttributeType.RuntimeVisibleParameterAnnotations, nameIndex, length);
	}
}
