package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.2">
 * ConstantValue Attribute</a>.
 * @author inagaki
 */
public class ConstantValue implements AttributeInfo {
    /**
     * constant value
     */
    public final String constantValue;

    ConstantValue(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.constantValue = extract(data.readShort(), pool);
    }

    @Override
    public String toString() {
        return "[ConstantValue]: " + constantValue;
    }
}