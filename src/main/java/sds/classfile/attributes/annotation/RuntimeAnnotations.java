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
 * {@link RuntimeVisibleAnnotations <code>RuntimeVisibleAnnotations</code>} and 
 * {@link RuntimeInvisibleAnnotations <code>RuntimeInvisibleAnnotations</code>}.
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