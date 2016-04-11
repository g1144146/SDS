package sds.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class VerificationTypeException extends Exception {
	public VerificationTypeException(int tag) {
		super(""+tag);
	}
}
