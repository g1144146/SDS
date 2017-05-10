package sds.classfile.attributes;

import sds.classfile.constantpool.ConstantInfo;
import static sds.util.DescriptorParser.parse;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9">
 * Signature Attribute</a>.
 * @author inagaki
 */
public class Signature extends AttributeInfo {
    private String signature;

    /**
     * constructor.
     */
    public Signature(int sigIndex, ConstantInfo[] pool) {
        super(AttributeType.Signature);
        this.signature = extract(sigIndex, pool);
    }

    /**
     * returns signature.
     * @return signature
     */
    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        String genericsType = parse(signature.substring(0, signature.lastIndexOf(">") + 1), true);
        String returnType = parse(signature.substring(signature.lastIndexOf(">") + 1));
        return super.toString() + ": " + genericsType + returnType;
    }
}