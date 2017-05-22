package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.10">
 * SourceFile Attribute</a>.
 * @author inagaki
 */
public class SourceFile implements AttributeInfo {
    /**
     * source file.
     */
    public final String sourceFile;

    SourceFile(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.sourceFile = extract(data.readShort(), pool);
    }

    @Override
    public String toString() {
        return "[SourceFile]: " + sourceFile;
    }
}