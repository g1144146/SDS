package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for enum constant as the value of this element-value.
 * @author inagaki
 */
public class EnumConstValue {
	/**
	 * constant-pool entry index of internal form of the binary name of the type of the enum constant.
	 */
	int typeNameIndex;
	/**
	 * constant-pool entry index of name of the enum constant.
	 */
	int constNameIndex;
	
	/**
	 * constructor
	 * @param raf classfile stream
	 * @throws IOException
	 */
	EnumConstValue(RandomAccessFile raf) throws IOException {
		this.typeNameIndex = raf.readShort();
		this.constNameIndex = raf.readShort();
	}

	/**
	 * returns constant-pool entry index of internal form of the binary name of the type of the enum constant.
	 * @return constant-pool entry index of internal form of the binary name of the type of the enum constant
	 */
	public int getTypeNameIndex()  {
		return typeNameIndex;
	}

	/**
	 * returns constant-pool entry index of name of the enum constant.
	 * @return constant-pool entry index of name of the enum constant
	 */
	public int getConstNameIndex() {
		return constNameIndex;
	}
}