package sds.classfile.attributes;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.13">
 * LocalVariableTable Attribute</a>.
 * @author inagaki
 */
public class LocalVariableTable extends LocalVariable {
	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public LocalVariableTable(int nameIndex, int length) {
		super(AttributeType.LocalVariableTable, nameIndex, length);
	}
}