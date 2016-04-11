package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class StackMapFrameException extends Exception {
	public StackMapFrameException(int tag) {
		super(""+tag);
	}

	public StackMapFrameException(String message) {
		super(message);
	}
}
