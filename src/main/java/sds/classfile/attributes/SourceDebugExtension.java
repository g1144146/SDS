package sds.classfile.attributes;

import java.io.IOException;
import java.util.StringJoiner;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.11">
 * SourceDebugExtension Attribute</a>.
 * @author inagakikenichi
 */
public class SourceDebugExtension extends AttributeInfo {
    private int[] debugExtension;

    SourceDebugExtension(int length, ClassFileStream data) throws IOException {
        super(AttributeType.SourceDebugExtension);
        this.debugExtension = new int[length];
        for(int i = 0; i < length; i++) {
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