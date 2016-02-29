package sophomore.classfile;

/**
 *
 * @author inagaki
 */
public class AccessFlags {
	/**
	 * class, field and method
	 * value: 1
	 */
	public static final int ACC_PUBLIC = 0x0001;
	/**
	 * field and method
	 * value: 2
	 */
	public static final int ACC_PRIVATE = 0x0002;
	/**
	 * field and method
	 * value: 4
	 */
	public static final int ACC_PROTECTED = 0x0004;
	/**
	 * field and method
	 * value: 8
	 */
	public static final int ACC_STATIC = 0x0008;
	/**
	 * class, field and method
	 * value: 16
	 */
	public static final int ACC_FINAL = 0x0010;
	/**
	 * class
	 * method
	 * value: 32
	 */
	public static final int ACC_SUPER        = 0x0020,
							ACC_SYNCHRONIZED = 0x0020;
	/**
	 * field
	 * method
	 * value: 64
	 */
	public static final int ACC_VOLATILE = 0x0040,
							ACC_BRIDGE   = 0x0040;
	/**
	 * field
	 * method
	 * value: 128
	 */
	public static final int ACC_TRANSIENT = 0x0080,
							ACC_VARARGS   = 0x0080;
	/**
	 * method
	 * value: 256
	 */
	public static final int ACC_NATIVE = 0x0100;
	/**
	 * class
	 * value: 512
	 */
	public static final int ACC_INTERFACE = 0x0200;
	/**
	 * class, method
	 * value: 1024
	 */
	public static final int ACC_ABSTRACT = 0x0400;
	/**
	 * method
	 * value: 2048
	 */
	public static final int ACC_STRICT = 0x0800;
	/**
	 * class, field and method
	 * value: 4096
	 */
	public static final int ACC_SYNTHETIC = 0x1000;
	/**
	 * class
	 * value: 8192
	 */
	public static final int ACC_ANNOTATION = 0x2000;
	/**
	 * class, field
	 * value: 16384
	 */
	public static final int ACC_ENUM = 0x4000;

	/**
	 * 
	 * @param key
	 * @param accessFlag
	 * @return 
	 */
	public static String get(String key, int accessFlag) {
		switch(accessFlag) {
			case ACC_PUBLIC:
				return "public";
			case ACC_PRIVATE:
				return "private";
			case ACC_PROTECTED:
				return "protected";
			case ACC_STATIC:
				return "static";
			case ACC_FINAL:
				return "final";
			case ACC_SUPER:
				if(key.equals("class")) return "super";
				else /* method */       return "synchronized";
			case ACC_VOLATILE:
				if(key.equals("field")) return "volatile";
				else /* method */       return "bridge";
			case ACC_TRANSIENT:
				if(key.equals("field")) return "transient";
				else /* method */       return "varargs";
			case ACC_NATIVE:
				return "native";
			case ACC_INTERFACE:
				return "interface";
			case ACC_ABSTRACT:
				return "abstract";
			case ACC_STRICT:
				return "strict";
			case ACC_SYNTHETIC:
				return "synthetic";
			case ACC_ANNOTATION:
				return "annotation";
			case ACC_ENUM:
				return "enum";
			default:
				System.out.println("invalid access flag: " + accessFlag);
				return " >>> unknown access flag <<<";
		}
	}
}
