package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for element-value pair in the annotation.
 * @author inagaki
 */
public class ElementValuePair {
    /**
     * constant-pool entry index of element name.
     */
    public final int elementNameIndex;
    /**
     * single element-value in the annotation.
     */
    public final ElementValue value;

    ElementValuePair(ClassFileStream data) throws IOException {
        this.elementNameIndex = data.readShort();
        this.value = new ElementValue(data);
    }
}