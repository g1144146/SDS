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
 * * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20">
 * RuntimeTypeInvisibleAnnotations Attribute</a>
 * and
 * * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.21">
 * RuntimeTypeInvisibleAnnotations Attribute</a>.
 * @author inagaki
 */
public class RuntimeTypeAnnotations extends AttributeInfo {
    private TypeAnnotation[] types;
    private String[] annotations;

    /**
     * constructor.
     * @param type attribute type
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public RuntimeTypeAnnotations(AttributeType type, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(type);
        this.types = new TypeAnnotation[data.readShort()];
        annotations = new String[types.length];
        for(int i = 0; i < types.length; i++) {
            types[i] = new TypeAnnotation(data);
            annotations[i] = parseAnnotation(types[i], new SDSStringBuilder(), pool);
        }
    }

    /**
     * returns runtime type annotation strings.
     * @return runtime type annotation strings
     */
    public String[] getAnnotations() {
        return annotations;
    }

    /**
     * returns runtime type annotations.
     * @return runtime type annotations
     */
    public TypeAnnotation[] getTypes() {
        return types;
    }
}