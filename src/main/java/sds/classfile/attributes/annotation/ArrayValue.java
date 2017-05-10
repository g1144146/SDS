package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for array as the value of this element-value.
 * @author inagaki
 */
public class ArrayValue {
    private ElementValue[] values;

    ArrayValue(ClassFileStream data) throws IOException {
        this.values = new ElementValue[data.readShort()];
        for(int i = 0; i < values.length; i++) {
            values[i] = new ElementValue(data);
        }
    }

    /**
     * returns array of element.
     * @return array of element
     */
    public ElementValue[] getValues() {
        return values;
    }
}