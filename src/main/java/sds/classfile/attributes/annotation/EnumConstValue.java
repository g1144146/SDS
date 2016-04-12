package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class EnumConstValue {
	/**
	 *
	 */
	int typeNameIndex;
	/**
	 *
	 */
	int constNameIndex;
	
	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	EnumConstValue(RandomAccessFile raf) throws IOException {
		this.typeNameIndex = raf.readShort();
		this.constNameIndex = raf.readShort();
	}
	public int getTypeNameIndex()  {
		return typeNameIndex;
	}
	public int getConstNameIndex() {
		return constNameIndex;
	}
}