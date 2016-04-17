package sds.classfile.attributes.stackmap;

/**
 * This interface is for verification_type_info which item of union of
 * {@link StackMapFrame <code>StackMapFrame</code>} has union.
 * @author inagaki
 */
public interface VerificationTypeInfo {
	/**
	 * returns discrimination tag of verification type info.
	 * @return discrimination tag
	 */
	public int getTag();
	/**
	 * returns type of verification type info.
	 * @return type of verification type info.
	 */
	public VerificationInfoType getType();
}