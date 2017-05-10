package sds.classfile.attributes.stackmap;

/**
 * This class is for exception of
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class VerificationTypeException extends RuntimeException {
    /**
     * constructor.
     * @param tag discrimination tag
     */
    public VerificationTypeException(int tag) {
        super(""+tag);
    }
}