package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This factory class is for
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class VerificationTypeInfoFactory {
    /**
     * returns verification type info.
     * @param data classfile stream
     * @return verification type info
     * @throws IOException
     */
    public VerificationTypeInfo create(ClassFileStream data) throws IOException {
        int tag = data.readUnsignedByte();
        switch(tag) {
            case 0: return new VerificationTypeInfo("top");
            case 1: return new VerificationTypeInfo("int");
            case 2: return new VerificationTypeInfo("float");
            case 3: return new VerificationTypeInfo("long");
            case 4: return new VerificationTypeInfo("double");
            case 5: return new VerificationTypeInfo("null");
            case 6: return new VerificationTypeInfo("this");
            case 7: return new ObjectVariableInfo(data.readShort());
            case 8: return new UninitializedVariableInfo(data.readShort());
            default: throw new VerificationTypeException(tag);
        }
    }

    class VerificationTypeException extends RuntimeException {
        VerificationTypeException(int tag) {
            super(Integer.toString(tag));
        }
    }
}