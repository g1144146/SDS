package sds.classfile;

/**
 * This class is for fields of class.
 * @author inagaki
 */
public class Fields extends Members<FieldInfo> {

	/**
	 * constructor.
	 * @param size field count
	 */
	public Fields(int size) {
		this.elements = new FieldInfo[size];
	}
}