package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.7">
 * EnclosingMethod Attribute</a>.
 * @author inagaki
 */
public class EnclosingMethod implements AttributeInfo {
    /**
     * class of enclosing method.
     */
    public final String _class;
    /**
     * enclosing method.
     */
    public final String method;
    
    EnclosingMethod(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this._class = extract(data.readShort(), pool);
        int methodIndex = data.readShort();
        this.method = methodIndex > 0 ? extract(methodIndex, pool) : "";
    }

    @Override
    public String toString() {
        return "[EnclosingMethod]: " + _class + "." + method;
    }
}