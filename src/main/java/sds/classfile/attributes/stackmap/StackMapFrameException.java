package sds.classfile.attributes.stackmap;

/**
 * This class is for exception of
 * {@link StackMapFrame <code>StackMapFrame</code>}.
 * @author inagaki
 */
public class StackMapFrameException extends RuntimeException {
    /**
     * constructor.
     * @param tag discrimination tag
     */
    public StackMapFrameException(int tag) {
        super(""+tag);
    }

    /**
     * constructor.
     * @param message message
     */
    public StackMapFrameException(String message) {
        super(message);
    }
}