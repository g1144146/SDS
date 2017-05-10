package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;
import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.classfile.attributes.annotation.AnnotationParser.parseAnnotation;

/**
 * This adapter class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.16">
 * RuntimeVisibleAnnotations Attribute</a>
 * and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.17">
 * RuntimeInvisibleAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeAnnotations extends AttributeInfo {
    private String[] annotations;

    /**
     * constructor.
     * @param type attribute type
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public RuntimeAnnotations(AttributeType type, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(type);
        this.annotations = new String[data.readShort()];
        for(int i = 0; i < annotations.length; i++) {
            annotations[i] = parseAnnotation(new Annotation(data), new SDSStringBuilder(), pool);
        }
    }

    /**
     * returns runtime annotations.
     * @return runtime annotations
     */
    public String[] getAnnotations() {
        return annotations;
    }
}