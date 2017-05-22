package sds.classfile.constantpool;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This factory class is for {@link ConstantInfo <code>ConstantInfo</code>}.
 * @author inagaki
 */
public class ConstantInfoFactory {
        /**
     * value of CONSTANT_Utf8.
     */
    public static final int C_UTF8 = 1;
    /**
     * value of CONSTANT_Integer.
     */
    public static final int C_INTEGER = 3;
    /**
     * value of CONSTANT_Float.
     */
    public static final int C_FLOAT = 4;
    /**
     * value of CONSTANT_Long.
     */
    public static final int C_LONG = 5;
    /**
     * value of CONSTANT_Double.
     */
    public static final int C_DOUBLE = 6;
    /**
     * value of CONSTANT_Class.
     */
    public static final int C_CLASS = 7;
    /**
     * value of CONSTANT_String.
     */
    public static final int C_STRING = 8;
    /**
     * value of CONSTANT_Fieldref.
     */
    public static final int C_FIELDREF = 9;
    /**
     * value of CONSTANT_Methodref.
     */
    public static final int C_METHODREF = 10;
    /**
     * value of CONSTANT_InterfaceMethodref.
     */
    public static final int C_INTERFACE_METHODREF = 11;
    /**
     * value of CONSTANT_NameAndType.
     */
    public static final int C_NAME_AND_TYPE = 12;

    /**
     * value of CONSTANT_MethodHandle.
     */
    public static final int C_METHOD_HANDLE = 15;
    /**
     * value of CONSTANT_MethodType.
     */
    public static final int C_METHOD_TYPE = 16;
    /**
     * value of CONSTANT_InvokeDynamic.
     */
    public static final int C_INVOKE_DYNAMIC = 18;

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
            case C_INTEGER:             return new NumberInfo(tag, data.readInt());
            case C_FLOAT:               return new NumberInfo(tag, data.readFloat());
            case C_LONG:                return new NumberInfo(tag, data.readLong());
            case C_DOUBLE:              return new NumberInfo(tag, data.readDouble());
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

    class ConstantTypeException extends RuntimeException {
        ConstantTypeException(int tag) {
            super("Tag " + tag + " is unknown.");
        }
    }
}