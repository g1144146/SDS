package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeTypeAnnotations <code>RuntimeVisibleTypeAnnotations</code>} and
 * @author inagaki
 */
public class TypeAnnotation extends Annotation {
    /**
     * type in a declaration or expression is annotated.
     */
    public final TargetInfo targetInfo;
    /**
     * part of the type indicated by target_info is annotated.
     */
    public final TypePath targetPath;

    TypeAnnotation(ClassFileStream data) throws IOException {
        TargetInfoFactory factory = new TargetInfoFactory();
        this.targetInfo = factory.create(data);
        this.targetPath = new TypePath(data);
        this.typeIndex = data.readShort();
        this.elementValuePairs = new ElementValuePair[data.readShort()];
        for(int i = 0; i < elementValuePairs.length; i++) {
            elementValuePairs[i] = new ElementValuePair(data);
        }
    }
}