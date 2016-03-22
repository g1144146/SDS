package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class VerificationTypeInfo {
	/**
	 * 
	 */
	int topVariableInfo;

	/**
	 * 
	 */
	int integerVariableInfo;

	/**
	 * 
	 */
	int floatVariableInfo;

	/**
	 * 
	 */
	int doubleVariableInfo;

	/**
	 * 
	 */
	int longVariableInfo;

	/**
	 * 
	 */
	int nullVariableInfo;

	/**
	 * 
	 */
	int uninitializedThisVariableInfo;

	/**
	 * ObjectVariableInfo tag
	 */
	int objectVariableInfo;
	/**
	 * ObjectVariableInfo cpool_index
	 */
	int cpoolIndex;

	/**
	 * UninitializedVariableInfo tag
	 */
	int uninitializedVariableInfo;
	/**
	 * UninitializeVariableInfo offset
	 */
	int offset;

	VerificationTypeInfo(RandomAccessFile raf) throws IOException {
		this.topVariableInfo = raf.readByte();
		this.integerVariableInfo = raf.readByte();
		this.floatVariableInfo = raf.readByte();
		this.doubleVariableInfo = raf.readByte();
		this.longVariableInfo = raf.readByte();
		this.nullVariableInfo = raf.readByte();
		this.uninitializedThisVariableInfo = raf.readByte();
		this.objectVariableInfo = raf.readByte();
		this.cpoolIndex = raf.readShort();
		this.uninitializedVariableInfo = raf.readByte();
		this.offset = raf.readShort();
	}

	/**
	 * 
	 * @param key
	 * @return 
	 */
	public int getNumber(String key) {
		switch(key) {
			case "top":     return topVariableInfo;
			case "integer": return integerVariableInfo;
			case "float":   return floatVariableInfo;
			case "double":  return doubleVariableInfo;
			case "long":    return longVariableInfo;
			case "null":    return nullVariableInfo;
			case "this":    return uninitializedThisVariableInfo;
			case "cpool":   return cpoolIndex;
			case "object":  return objectVariableInfo;
			case "val":     return uninitializedVariableInfo;
			case "offset":  return offset;
			default:
				System.out.println("unknown key: " + key);
				return -10000;
		}
	}
}
