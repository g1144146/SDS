package sds.classfile.attributes;

import java.io.IOException;
import java.util.StringJoiner;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.11">
 * SourceDebugExtension Attribute</a>.
 * @author inagakikenichi
 */
public class SourceDebugExtension extends AttributeInfo {
    private int[] debugExtension;
    private int attrLen;

    /**
     * constructor.
     * @param length debug extension length
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public SourceDebugExtension(int length, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.SourceDebugExtension);
        this.attrLen = length;
        this.debugExtension = new int[attrLen];
        for(int i = 0; i < debugExtension.length; i++) {
            debugExtension[i] = data.readUnsignedByte();
        }
    }

    /**
     * returns constant-pool entry index of debugging information.
     * @return constant-pool entry index of debugging information
     */
    public int[] getDebugExtension() {
        return debugExtension;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for(int ex : debugExtension) {
            sj.add(ex + "");
        }
        return super.toString() + ": " + sj.toString();
    }
}