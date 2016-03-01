package sophomore.classfile.constantpool;


/**
 * 
 * @author inagaki
 */
public class ConstantType {
	/**
	 * 
	 */
	public static final int C_UTF8 = 1;
	/**
	 * 
	 */
	public static final int C_INTEGER = 3;
	/**
	 * 
	 */
	public static final int C_FLOAT = 4;
	/**
	 * 
	 */
	public static final int C_LONG = 5;
	/**
	 * 
	 */
	public static final int C_DOUBLE = 6;
	/**
	 * 
	 */
	public static final int C_CLASS = 7;
	/**
	 * 
	 */
	public static final int C_STRING = 8;
	/**
	 * 
	 */
	public static final int C_FIELDREF = 9;
	/**
	 * 
	 */
	public static final int C_METHODREF = 10;
	/**
	 * 
	 */
	public static final int C_INTERFACE_METHODREF = 11;
	/**
	 * 
	 */
	public static final int C_NAME_AND_TYPE = 12;

	/**
	 * 
	 */
	public static final int C_METHOD_HANDLE = 15;
	/**
	 * 
	 */
	public static final int C_METHOD_TYPE = 16;
	/**
	 * 
	 */
	public static final int C_INVOKE_DYNAMIC = 18;

	public static String get(int type) {
		switch(type) {
			case C_UTF8:
				return "CONSTANT_UTF8";
			case C_INTEGER:
				return "CONSTANT_INTEGER";
			case C_FLOAT:
				return "CONSTANT_FLOAT";
			case C_LONG:
				return "CONSTANT_LONG";
			case C_DOUBLE:
				return "CONSTANT_DOUBLE";
			case C_CLASS:
				return "CONSTANT_CLASS";
			case C_STRING:
				return "CONSTANT_STRING";
			case C_FIELDREF:
				return "CONSTANT_FIELDREF";
			case C_METHODREF:
				return "CONSTANT_METHODREF";
			case C_INTERFACE_METHODREF:
				return "CONSTANT_INTERFACE_METHODREF";
			case C_NAME_AND_TYPE:
				return "CONSTANT_NAME_AND_TYPE";
			case C_METHOD_HANDLE:
				return "CONSTANT_METHOD_HANDLE";
			case C_METHOD_TYPE:
				return "CONSTANT_METHOD_TYPE";
			case C_INVOKE_DYNAMIC:
				return "CONSTANT_INVOKE_DYNAMIC";
			default:
				System.out.println("invalid type: " + type);
				return " >>> unknown type <<<";
		}
	}
}