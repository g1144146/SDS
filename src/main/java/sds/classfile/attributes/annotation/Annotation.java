package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeAnnotations <code>RuntimeInvisibleAnnotations</code>} have item.
 * @author inagaki
 */
public class Annotation {
    /**
     * constant-pool entry index of annotation type.
     */
    public final int typeIndex;
    /**
     * element-value pair in the annotation.
     */
    public final ElementValuePair[] elementValuePairs;
    
    Annotation(ClassFileStream data) throws IOException {
        this.typeIndex = data.readShort();
        this.elementValuePairs = new ElementValuePair[data.readShort()];
        for(int i = 0; i < elementValuePairs.length; i++) {
            elementValuePairs[i] = new ElementValuePair(data);
        }
    }
}