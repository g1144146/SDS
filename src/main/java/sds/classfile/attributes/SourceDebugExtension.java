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
public class SourceDebugExtension implements AttributeInfo {
    /**
     * constant-pool entry index of debugging information.
     */
    public final int[] debugExtension;

    SourceDebugExtension(int length, ClassFileStream data) throws IOException {
        this.debugExtension = new int[length];
        for(int i = 0; i < length; i++) {
            debugExtension[i] = data.readUnsignedByte();
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for(int ex : debugExtension) {
            sj.add(Integer.toString(ex));
        }
        return "[SourceDebugExtension]: " + sj.toString();
    }
}