package sds.classfile;

/**
 * This class is for methods of class.
 * @author inagaki
 */
public class Methods extends Members<MethodInfo> {
	/**
	 * constructor.
	 * @param size method count
	 */
	public Methods(int size) {
		this.elements = new MethodInfo[size];
	}
}