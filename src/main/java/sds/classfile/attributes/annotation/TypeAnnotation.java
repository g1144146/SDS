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
    
    TypeAnnotation(TargetInfo targetInfo, TypePath targetPath, ClassFileStream data) throws IOException {
        super(data);
        this.targetInfo = targetInfo;
        this.targetPath = targetPath;
    }
}