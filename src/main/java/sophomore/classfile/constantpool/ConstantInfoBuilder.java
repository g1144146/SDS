package sophomore.classfile.constantpool;

import static sophomore.classfile.constantpool.ConstantType.*;

public class ConstantInfoBuilder {
	private static ConstantInfoBuilder builder = new ConstantInfoBuilder();
	
	public static ConstantInfoBuilder getInstance() {
		return builder;
	}

	public ConstantInfo build(int tag) throws UnknownConstantTypeException {
		switch(tag) {
			case C_Class:
				return new ClassInfo();
			case C_Fieldref:
				return new FieldrefInfo();
			case C_Methodref:
				return new MethodrefInfo();
			case C_InterfaceMethodref:
				return new InterfaceMethodrefInfo();
			case C_Integer:
				return new IntegerInfo();
			case C_Float:
				return new FloatInfo();
			case C_Long:
				return new LongInfo();
			case C_Double:
				return new DoubleInfo();
			case C_String:
				return new StringInfo();
			case C_NameAndType:
				return new NameAndTypeInfo();
			case C_Utf8:
				return new Utf8Info();
			case C_MethodHandle:
				return new MethodHandleInfo();
			case C_MethodType:
				return new MethodTypeInfo();
			case C_InvokeDynamic:
				return new InvokeDynamicInfo();
			default:
				System.out.println("Invalid tag: " + tag);
				break;
		}
		throw new UnknownConstantTypeException(tag);
	}
}