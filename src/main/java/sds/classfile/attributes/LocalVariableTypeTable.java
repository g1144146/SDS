package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class LocalVariableTypeTable extends LocalVariable {

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public LocalVariableTypeTable(int nameIndex, int length) {
		super(AttributeType.LocalVariableTypeTable, nameIndex, length);
	}
}
