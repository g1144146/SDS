package sds.classfile.attributes.annotation;

/**
 * This class for exception of {@link ElementValue <code>ElementValue</code>}
 * @author inagaki
 */
public class ElementValueException extends Exception {
	/**
	 * constructor.
	 * @param tag ASCII character to indicate the type of the value of the element-value
	 */
	public ElementValueException(char tag) {
		super(""+tag);
	}
}
