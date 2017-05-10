package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.2">
 * type_path</a>.<br>
 * This item is one of {@link TypeAnnotation <code>TypeAnnotation</code>}.<br>
 * @author inagaki
 */
public class TypePath {
    private int[] typePathKind;
    private int[] typeArgIndex;

    TypePath(ClassFileStream data) throws IOException {
        this.typeArgIndex = new int[data.readUnsignedByte()];
        this.typePathKind = new int[typeArgIndex.length];
        for(int i = 0; i < typeArgIndex.length; i++) {
            this.typePathKind[i] = data.readByte();
            this.typeArgIndex[i] = data.readByte();
        }
    }

    /**
     * returns kind index of location of the annotation.
     * the kind index of location of the annotation in an array type,
     * nested type, or parameterized type.<br>
     * 0~3.
     * @return  kind index of location of the annotation
     */
    public int[] getPathKind() {
        return typePathKind;
    }

    /**
     * returns index of type argument of a parameterized type is annotated.
     * @return index of type argument
     */
    public int[] getArgIndex() {
        return typeArgIndex;
    }
}
