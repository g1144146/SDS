package sophomore.classfile.attributes;

/**
 *
 * @author inagaki
 */
public class LocalVariableTable extends LocalVariable {

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public LocalVariableTable(int nameIndex, int length) {
		super(AttributeType.Type.LocalVariableTable, nameIndex, length);
	}
}
