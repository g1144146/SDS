package sophomore.classfile.constantpool;

import static sophomore.classfile.constantpool.ConstantType.*;

/**
 * 
 * @author inagaki
 */
public class ConstantInfoBuilder {
	/**
	 * 
	 */
	private static ConstantInfoBuilder builder = new ConstantInfoBuilder();

	/**
	 * 
	 */
	private ConstantInfoBuilder() {}

	/**
	 * 
	 * @return 
	 */
	public static ConstantInfoBuilder getInstance() {
		return builder;
	}

	/**
	 * 
	 * @param tag
	 * @return
	 * @throws UnknownConstantTypeException 
	 */
	public ConstantInfo build(int tag) throws UnknownConstantTypeException {
		switch(tag) {
			case C_CLASS:
				return new ClassInfo();
			case C_FIELDREF:
				return new FieldrefInfo();
			case C_METHODREF:
				return new MethodrefInfo();
			case C_INTERFACE_METHODREF:
				return new InterfaceMethodrefInfo();
			case C_INTEGER:
				return new IntegerInfo();
			case C_FLOAT:
				return new FloatInfo();
			case C_LONG:
				return new LongInfo();
			case C_DOUBLE:
				return new DoubleInfo();
			case C_STRING:
				return new StringInfo();
			case C_NAME_AND_TYPE:
				return new NameAndTypeInfo();
			case C_UTF8:
				return new Utf8Info();
			case C_METHOD_HANDLE:
				return new MethodHandleInfo();
			case C_METHOD_TYPE:
				return new MethodTypeInfo();
			case C_INVOKE_DYNAMIC:
				return new InvokeDynamicInfo();
			default:
				System.out.println("Invalid tag: " + tag);
				break;
		}
		throw new UnknownConstantTypeException(tag);
	}
}