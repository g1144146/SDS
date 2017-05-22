package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for enum constant as the value of this element-value.
 * @author inagaki
 */
public class EnumConstValue {
    /**
     * constant-pool entry index of internal form of the binary name of the type of the enum constant.
     */
    public final int typeNameIndex;
    /**
     * constant-pool entry index of name of the enum constant.
     */
    public final int constNameIndex;

    EnumConstValue(ClassFileStream data) throws IOException {
        this.typeNameIndex = data.readShort();
        this.constNameIndex = data.readShort();
    }
}