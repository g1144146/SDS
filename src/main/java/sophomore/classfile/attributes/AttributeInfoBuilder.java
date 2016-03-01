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
	 * @return 
	 */
//	public AttributeInfo build() {
//		
//	}
}
