package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;  
import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.classfile.attributes.annotation.AnnotationParser.parseElementValue;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.22">AnnotationDefault Attribute</a>.
 * @author inagaki
 */
public class AnnotationDefault implements AttributeInfo {
    /**
     * default value of the annotation type element.
     */
    public final String defaultValue;

    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public AnnotationDefault(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        ElementValue value = new ElementValue(data);
        this.defaultValue = parseElementValue(value, new SDSStringBuilder(), pool);
    }

    @Override
    public String toString() {
        return "[AnnotationDefault]: " + defaultValue;
    }
}