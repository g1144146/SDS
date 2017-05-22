package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.classfile.attributes.annotation.AnnotationParser.parseAnnotation;

/**
 * This adapter class is for
 * * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20">
 * RuntimeTypeInvisibleAnnotations Attribute</a>
 * and
 * * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.21">
 * RuntimeTypeInvisibleAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeTypeAnnotations implements AttributeInfo {
    /**
     * runtime type annotations.
     */
    public final TypeAnnotation[] types;
    /**
     * runtime type annotation strings.
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
    public RuntimeTypeAnnotations(String name, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.name = name;
        this.types = new TypeAnnotation[data.readShort()];
        annotations = new String[types.length];
        TargetInfoFactory factory = new TargetInfoFactory();
        for(int i = 0; i < types.length; i++) {
            types[i] = new TypeAnnotation(factory.create(data), new TypePath(data), data);
            annotations[i] = parseAnnotation(types[i], new SDSStringBuilder(), pool);
        }
    }
}