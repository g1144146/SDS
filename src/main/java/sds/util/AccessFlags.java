package sds.util;

/**
 * This class is for access flag.
 * @author inagaki
 */
public class AccessFlags {
	/**
	 * hex of public.
	 * class, field and method
	 * value: 1
	 */
	public static final int ACC_PUBLIC = 0x0001;
	/**
	 * hex of private.
	 * field and method
	 * value: 2
	 */
	public static final int ACC_PRIVATE = 0x0002;
	/**
	 * hex of protected.
	 * field and method
	 * value: 4
	 */
	public static final int ACC_PROTECTED = 0x0004;
	/**
	 * hex of static.
	 * field and method
	 * value: 8
	 */
	public static final int ACC_STATIC = 0x0008;
	/**
	 * hex of final.
	 * class, field and method
	 * value: 16
	 */
	public static final int ACC_FINAL = 0x0010;
	/**
	 * hex of super, synchronized.
	 * class
	 * method
	 * value: 32
	 */
	public static final int ACC_SUPER        = 0x0020,
							ACC_SYNCHRONIZED = 0x0020;
	/**
	 * hex of valtile, bridge.
	 * field
	 * method
	 * value: 64
	 */
	public static final int ACC_VOLATILE = 0x0040,
							ACC_BRIDGE   = 0x0040;
	/**
	 * hex of transient, varargs.
	 * field
	 * method
	 * value: 128
	 */
	public static final int ACC_TRANSIENT = 0x0080,
							ACC_VARARGS   = 0x0080;
	/**
	 * hex of native.
	 * method
	 * value: 256
	 */
	public static final int ACC_NATIVE = 0x0100;
	/**
	 * interface.
	 * class
	 * value: 512
	 */
	public static final int ACC_INTERFACE = 0x0200;
	/**
	 * hex of abstract.
	 * class, method
	 * value: 1024
	 */
	public static final int ACC_ABSTRACT = 0x0400;
	/**
	 * hex of strict.
	 * method
	 * value: 2048
	 */
	public static final int ACC_STRICT = 0x0800;
	/**
	 * synthetic.
	 * class, field and method
	 * value: 4096
	 */
	public static final int ACC_SYNTHETIC = 0x1000;
	/**
	 * hex of annotation.
	 * class
	 * value: 8192
	 */
	public static final int ACC_ANNOTATION = 0x2000;
	/**
	 * hex of enum.
	 * class, field
	 * value: 16384
	 */
	public static final int ACC_ENUM = 0x4000;
	/**
	 * access flags for class.
	 */
	public static final int CLASS_FLAG = ACC_PUBLIC | ACC_FINAL | ACC_SUPER
		| ACC_INTERFACE | ACC_ABSTRACT | ACC_SYNTHETIC | ACC_ANNOTATION | ACC_ENUM;
	/**
	 * access flags for field.
	 */
	public static final int FIELD_FLAG = ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED
		| ACC_STATIC | ACC_FINAL | ACC_VOLATILE | ACC_TRANSIENT | ACC_SYNTHETIC | ACC_ENUM;
	/**
	 * access flags for method.
	 */
	public static final int METHOD_FLAG = ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED
		| ACC_STATIC | ACC_FINAL | ACC_SYNCHRONIZED | ACC_BRIDGE | ACC_VARARGS
		| ACC_NATIVE | ACC_ABSTRACT | ACC_STRICT | ACC_SYNTHETIC;
	/**
	 * access flags for nested class.
	 */
	public static final int NESTED_CLASS = ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC
		| ACC_FINAL | ACC_INTERFACE | ACC_ABSTRACT | ACC_SYNTHETIC | ACC_ANNOTATION | ACC_ENUM;

	/**
	 * returns access flags.
	 * @param accessFlag bit sequence of access flag 
	 * @param type type name
	 * @return access flags
	 */
	public static String get(int accessFlag, String type) {
		if(type.equals("class") && ((accessFlag | CLASS_FLAG) == CLASS_FLAG)) {
			return getClassAccessFlag(accessFlag);
		} else if(type.equals("field")  && (accessFlag | FIELD_FLAG  ) == FIELD_FLAG) {
			return getFieldAccessFlag(accessFlag);
		} else if(type.equals("method") && (accessFlag | METHOD_FLAG ) == METHOD_FLAG) {
			return getMethodAccessFlag(accessFlag);
		} else if(type.equals("nested") && (accessFlag | NESTED_CLASS) == NESTED_CLASS) {
			return getClassAccessFlag(accessFlag);
		}
		System.out.println("unknown access flag : " + accessFlag);
		System.out.println("type                : " + type);
		return " >>> unknown access flag <<<";
	}

	private static String getClassAccessFlag(int accessFlag) {
		StringBuilder sb = new StringBuilder();
		if(checkFlag(accessFlag, ACC_PUBLIC))     sb.append("public ");
		if(checkFlag(accessFlag, ACC_FINAL))      sb.append("final ");
		if(checkFlag(accessFlag, ACC_SYNTHETIC))  sb.append("synthetic ");
		if(checkFlag(accessFlag, ACC_ANNOTATION)) sb.append("@interface ");
		if(checkFlag(accessFlag, ACC_ENUM))       sb.append("enum ");
		if(checkFlag(accessFlag, ACC_INTERFACE) && ((accessFlag & ACC_ANNOTATION) == 0)) {
			sb.append("interface ");
		}
		if((accessFlag & (ACC_INTERFACE | ACC_ENUM | ACC_ANNOTATION)) == 0) {
			sb.append("class ");
		}
		return sb.toString();
	}

	private static String getFieldAccessFlag(int accessFlag) {
		StringBuilder sb = new StringBuilder();
		if(checkFlag(accessFlag, ACC_PUBLIC))     sb.append("public ");
		if(checkFlag(accessFlag, ACC_PRIVATE))    sb.append("private ");
		if(checkFlag(accessFlag, ACC_PROTECTED))  sb.append("protected ");
		if(checkFlag(accessFlag, ACC_STATIC))     sb.append("static ");
		if(checkFlag(accessFlag, ACC_FINAL))      sb.append("final ");
		if(checkFlag(accessFlag, ACC_VOLATILE))   sb.append("volatile ");
		if(checkFlag(accessFlag, ACC_TRANSIENT))  sb.append("transient ");
		if(checkFlag(accessFlag, ACC_SYNTHETIC))  sb.append("synthetic ");
		if(checkFlag(accessFlag, ACC_ENUM))       sb.append("enum ");
		return sb.toString();
	}

	private static String getMethodAccessFlag(int accessFlag) {
		StringBuilder sb = new StringBuilder();
		if(checkFlag(accessFlag, ACC_PUBLIC))         sb.append("public ");
		if(checkFlag(accessFlag, ACC_PRIVATE))        sb.append("private ");
		if(checkFlag(accessFlag, ACC_PROTECTED))      sb.append("protected ");
		if(checkFlag(accessFlag, ACC_STATIC))         sb.append("static ");
		if(checkFlag(accessFlag, ACC_FINAL))          sb.append("final ");
		if(checkFlag(accessFlag, ACC_SYNCHRONIZED))   sb.append("synchronized ");
		if(checkFlag(accessFlag, ACC_BRIDGE))         sb.append("bridge ");
		if(checkFlag(accessFlag, ACC_NATIVE))         sb.append("native ");
		if(checkFlag(accessFlag, ACC_ABSTRACT))       sb.append("abstract ");
		if(checkFlag(accessFlag, ACC_STRICT))         sb.append("strict ");
		if(checkFlag(accessFlag, ACC_SYNTHETIC))      sb.append("synthetic ");
		return sb.toString();
	}

	private static boolean checkFlag(int accessFlag, int target) {
		return (accessFlag & target) == target;
	}
}
