package sds.classfile.constantpool;

/**
 * This class is for exception of {@link ConstantInfo <code>ConstantInfo</code>}.
 * @author inagaki
 */
public class ConstantTypeException extends RuntimeException {
	/**
	 * constructor.
	 * @param tag constant info tag.
	 */
	public ConstantTypeException(int tag) {
		super("Tag " + tag + " is unknown.");
	}
}