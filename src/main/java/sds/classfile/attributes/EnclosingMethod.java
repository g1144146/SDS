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
public class EnclosingMethod extends AttributeInfo {
    private String _class;
    private String method;
    
    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public EnclosingMethod(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.EnclosingMethod);
        this._class = extract(data.readShort(), pool);
        int methodIndex = data.readShort();
        this.method = methodIndex > 0 ? extract(methodIndex, pool) : "";
    }

    /**
     * returns class of enclosing method.
     * @return class
     */
    public String getEncClass() {
        return _class;
    }

    /**
     * returns enclosing method.
     * @return method
     */
    public String getEncMethod() {
        return method;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + _class + "." + method;
    }
}