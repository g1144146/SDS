package sds.classfile.attributes;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.14">
 * LocalVariableTypeTable Attribute</a>.
 */
public class LocalVariableTypeTable extends LocalVariable {
	/**
	 * constructor.
	 */
	public LocalVariableTypeTable() {
		super(AttributeType.LocalVariableTypeTable);
	}
}