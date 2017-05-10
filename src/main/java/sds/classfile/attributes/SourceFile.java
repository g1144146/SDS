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
public class SourceFile extends AttributeInfo {
    private String sourceFile;

    SourceFile(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.SourceFile);
        this.sourceFile = extract(data.readShort(), pool);
    }

    /**
     * returns source file.
     * @return source file
     */
    public String getSourceFile() {
        return sourceFile;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + sourceFile;
    }
}