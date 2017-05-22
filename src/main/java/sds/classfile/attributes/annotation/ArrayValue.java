package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for array as the value of this element-value.
 * @author inagaki
 */
public class ArrayValue {
    /**
     * array of element.
     */
    public final ElementValue[] values;

    ArrayValue(ClassFileStream data) throws IOException {
        this.values = new ElementValue[data.readShort()];
        for(int i = 0; i < values.length; i++) {
            values[i] = new ElementValue(data);
        }
    }
}