package sds.classfile.attributes.stackmap;

/**
 * This interface is for stack_map_frame which
 * {@link StackMapTable <code>StackMapTable</code>} has union.
 * @author inagaki
 */
public interface StackMapFrame {
	/**
	 * returns type of stack-map-frame.
	 * @return type
	 */
	abstract StackMapFrameType getFrameType();

	/**
	 * returns discrimination tag of item of union.
	 * @return discrimination tag
	 */
	abstract int getTag();
}