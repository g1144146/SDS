package sds.classfile.attributes.annotation;

/**
 * This class is for exception of {@link TargetInfo <code>TargetInfo</code>}.
 * @author inagaki
 */
public class TargetTypeException extends Exception {
	/**
	 * constructor.
	 * @param targetType target type byte
	 */
	public TargetTypeException(int targetType) {
		super("" + targetType);
	}
}
