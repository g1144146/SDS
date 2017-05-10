package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.classfile.attributes.annotation.AnnotationParser.parseAnnotation;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeParameterAnnotations <code>RuntimeVisibleParameterAnnotations</code>}
 * have item.
 * @author inagaki
 */
public class ParameterAnnotations {
    private String[] annotations;

    ParameterAnnotations(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.annotations = new String[data.readShort()];
        for(int i = 0; i < annotations.length; i++) {
            annotations[i] = parseAnnotation(new Annotation(data), new SDSStringBuilder(), pool);
        }
    }

    /**
     * returns runtime parameter annotations.
     * @return runtime parameter annotations
     */
    public String[] getAnnotations() {
        return annotations;
    }
}