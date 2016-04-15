package sds.classfile.attributes;

/**
 * This class is for exception of {@link AttributeInfo <code>AttributeInfo</code>}.
 * @author inagaki
 */
public class AttributeTypeException extends Exception {
	/**
	 * constructor.
	 * @param type attribute name
	 */
	public AttributeTypeException(String type) {
		super(type);
	}
}
