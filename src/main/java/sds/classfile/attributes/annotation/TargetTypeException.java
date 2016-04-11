package sds.classfile.attributes.annotation;

/**
 *
 * @author inagaki
 */
public class TargetTypeException extends Exception {
	public TargetTypeException(int targetType) {
		super("" + targetType);
	}
}
