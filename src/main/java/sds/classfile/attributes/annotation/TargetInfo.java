package sds.classfile.attributes.annotation;

/**
 * This interface is for target info.<br>
 * {@link TypeAnnotation <code>TypeAnnotation</code>} has union.
 * @author inagaki
 */
public interface TargetInfo {
	/**
	 * returns target info type.
	 * @return target info type
	 */
	public TargetInfoType getType();
}