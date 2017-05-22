package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.classfile.attributes.annotation.AnnotationParser.parseAnnotation;

/**
 * This adapter class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.18">
 * RuntimeParameterInvisibleAnnotations Attribute</a>
 * and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.19">
 * RuntimeParameterInvisibleAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeParameterAnnotations implements AttributeInfo {
    /**
     * runtime parameter annotations.
     */
    public final String[][] parameterAnnotations;
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
    public RuntimeParameterAnnotations(String name, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.name = name;
        this.parameterAnnotations = new String[data.readByte()][];
        for(int i = 0; i < parameterAnnotations.length; i++) {
            String[] annotations = new String[data.readShort()];
            for(int j = 0; j < annotations.length; j++) {
                annotations[j] = parseAnnotation(new Annotation(data), new SDSStringBuilder(), pool);
            }
            parameterAnnotations[i] = annotations;
        }
    }
}