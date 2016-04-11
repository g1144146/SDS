package sds.classfile;

/**
 * 
 * @author inagaki
 */
public class Fields extends Members<FieldInfo> {

	/**
	 * 
	 * @param size
	 */
	public Fields(int size) {
		this.elements = new FieldInfo[size];
	}
}