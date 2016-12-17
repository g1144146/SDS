package sds.classfile.constantpool;

import static sds.classfile.constantpool.ConstantType.*;

/**
 * This factory class is for {@link ConstantInfo <code>ConstantInfo</code>}.
 * @author inagaki
 */
public class ConstantInfoFactory {
	/**
	 * returns constant info.
	 * @param tag constant info tag.
	 * @return constant info
	 * @throws ConstantTypeException 
	 */
	public ConstantInfo create(int tag) throws ConstantTypeException {
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