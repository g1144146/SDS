package sophomore.classfile.attributes;

/**
 *
 * @author inagaki
 */
public class UnknownAttributeTypeException extends Exception {

	/**
	 * 
	 * @param type 
	 */
	public UnknownAttributeTypeException(String type) {
		super(type);
	}
}
