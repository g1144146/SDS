package sds.classfile.constantpool;

import static sds.classfile.constantpool.ConstantType.*;

/**
 * This builder class is for {@link ConstantInfo <code>ConstantInfo</code>}.<br>
 * This class is designed singleton.
 * @author inagaki
 */
public class ConstantInfoBuilder {
	/**
	 * builder class of instance.
	 */
	private static ConstantInfoBuilder builder = new ConstantInfoBuilder();

	/**
	 * constructor.
	 */
	private ConstantInfoBuilder() {}

	/**
	 * return own instance.
	 * @return instance
	 */
	public static ConstantInfoBuilder getInstance() {
		return builder;
	}

	/**
	 * returns constant info.
	 * @param tag constant info tag.
	 * @return constant info
	 * @throws ConstantTypeException 
	 */
	public ConstantInfo build(int tag) throws ConstantTypeException {
		switch(tag) {
			case C_CLASS:               return new ClassInfo();
			case C_FIELDREF:            return new FieldrefInfo();
			case C_METHODREF:           return new MethodrefInfo();
			case C_INTERFACE_METHODREF: return new InterfaceMethodrefInfo();
			case C_INTEGER:             return new IntegerInfo();
			case C_FLOAT:               return new FloatInfo();
			case C_LONG:                return new LongInfo();
			case C_DOUBLE:              return new DoubleInfo();
			case C_STRING:              return new StringInfo();
			case C_NAME_AND_TYPE:       return new NameAndTypeInfo();
			case C_UTF8:                return new Utf8Info();
			case C_METHOD_HANDLE:       return new MethodHandleInfo();
			case C_METHOD_TYPE:         return new MethodTypeInfo();
			case C_INVOKE_DYNAMIC:      return new InvokeDynamicInfo();
			default:                    throw  new ConstantTypeException(tag);
		}
	}
}