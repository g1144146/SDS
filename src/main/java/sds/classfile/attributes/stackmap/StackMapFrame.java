package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public interface StackMapFrame {
	/**
	 * 
	 * @return 
	 */
	public StackMapFrameType getFrameType();

	/**
	 * 
	 * @return 
	 */
	public int getTag();
}
