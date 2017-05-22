package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;
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
public class RuntimeAnnotations implements AttributeInfo {
    /**
     * runtime annotations.
     */
    public final String[] annotations;
    /**
     * attribute name.
     */
    public final String name;

    /**
     * constructor.
     * @param name attribute name
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public RuntimeAnnotations(String name, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.name = name;
        this.annotations = new String[data.readShort()];
        for(int i = 0; i < annotations.length; i++) {
            annotations[i] = parseAnnotation(new Annotation(data), new SDSStringBuilder(), pool);
        }
    }
}