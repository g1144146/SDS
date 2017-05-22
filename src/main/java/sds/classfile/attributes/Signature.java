package sds.classfile.attributes;

import sds.classfile.constantpool.ConstantInfo;
import static sds.util.DescriptorParser.parse;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9">
 * Signature Attribute</a>.
 * @author inagaki
 */
public class Signature implements AttributeInfo {
    /**
     * signature.
     */
    public final String signature;

    Signature(int sigIndex, ConstantInfo[] pool) {
        this.signature = extract(sigIndex, pool);
    }

    @Override
    public String toString() {
        String genericsType = parse(signature.substring(0, signature.lastIndexOf(">") + 1), true);
        String returnType = parse(signature.substring(signature.lastIndexOf(">") + 1));
        return "[Signature]: " + genericsType + returnType;
    }
}