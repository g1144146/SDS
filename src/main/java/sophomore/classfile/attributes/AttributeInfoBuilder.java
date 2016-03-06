package sophomore.classfile.attributes;

/**
 *
 * @author inagaki
 */
public class AttributeInfoBuilder {
	/**
	 * 
	 */
	private static AttributeInfoBuilder builder = null;

	/**
	 * 
	 * @return 
	 */
	public static AttributeInfoBuilder getInstance() {
		if(builder == null) {
			builder = new AttributeInfoBuilder();
		}
		return builder;
	}

	/**
	 * 
	 * @param attrName
	 * @return 
	 */
	public AttributeInfo build(String attrName, int nameIndex, int length) {
		return null;
	}
}
