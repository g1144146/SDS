package sds.classfile.constantpool;

import java.io.IOException;
import sds.classfile.ClassFileStream;

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
	 */
	public ConstantInfo create(int tag, ClassFileStream data) throws IOException {
		switch(tag) {
			case C_CLASS:               return new ClassInfo(data.readShort());
			case C_FIELDREF:
			case C_METHODREF:
			case C_INTERFACE_METHODREF: return new MemberRefInfo(tag, data.readShort(), data.readShort());
			case C_INTEGER:             return new IntInfo(data.readInt());
			case C_FLOAT:               return new FloatInfo(data.readFloat());
			case C_LONG:                return new LongInfo(data.readLong());
			case C_DOUBLE:              return new DoubleInfo(data.readDouble());
			case C_STRING:              return new StringInfo(data.readShort());
			case C_NAME_AND_TYPE:       return new NameAndTypeInfo(data.readShort(), data.readShort());
			case C_UTF8:
                int len = data.readShort();
                byte[] b = data.readFully(new byte[len]);
                return new Utf8Info(new String(b, "UTF-8"));
			case C_METHOD_HANDLE:       return new MethodHandleInfo(data.readByte(), data.readShort());
			case C_METHOD_TYPE:         return new MethodTypeInfo(data.readShort());
			case C_INVOKE_DYNAMIC:      return new InvokeDynamicInfo(data.readShort(), data.readShort());
			default:                    throw  new ConstantTypeException(tag);
		}
	}
}