package sds.classfile;

/**
 * 
 * @author inagaki
 */
public class Methods extends Members<MethodInfo> {
	/**
	 * 
	 * @param size 
	 */
	public Methods(int size) {
		this.elements = new MethodInfo[size];
	}
}