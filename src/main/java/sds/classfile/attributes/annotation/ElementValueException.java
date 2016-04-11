package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class ElementValueException extends Exception {
	public ElementValueException(int tag) {
		super(""+tag);
	}
}
