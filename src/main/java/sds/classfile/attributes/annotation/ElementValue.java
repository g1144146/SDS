package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for single element-value in the annotation.
 * @author inagaki
 */
public class ElementValue {
    private char tag;
    private int constValueIndex;
    private EnumConstValue enumConstValue;
    private int classInfoIndex;
    private Annotation annotationValue;
    private ArrayValue arrayValue;

    ElementValue(ClassFileStream data) throws IOException, ElementValueException {
        this.tag = (char)data.readByte();
        switch(tag) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 's': this.constValueIndex = data.readShort();         break;
            case 'e': this.enumConstValue  = new EnumConstValue(data); break;
            case 'c': this.classInfoIndex  = data.readShort();         break;
            case '@': this.annotationValue = new Annotation(data);     break;
            case '[': this.arrayValue      = new ArrayValue(data);     break;
            default:  throw new ElementValueException(tag);
        }
    }

    /**
     * returns ASCII character to indicate the type of the value
     * of the element-value.
     * @return ASCII character
     */
    public char getTag() {
        return tag;
    }

    /**
     * returns primitive constant value or a String literal
     * as the value of this element-value.
     * @return primitive constant value
     */
    public int getConstValueIndex() {
        return constValueIndex;
    }

    /**
     * returns enum constant as the value of this element-value.
     * @return enum constant
     */
    public EnumConstValue getEnumConstValue() {
        return enumConstValue;
    }

    /**
     * returns constant-pool entry index of class.
     * @return constant-pool entry index of class
     */
    public int getClassInfoIndex() {
        return classInfoIndex;
    }

    /**
     * returns nested annotation.
     * @return nested annotation
     */
    public Annotation getAnnotationValue() {
        return annotationValue;
    }

    /**
     * returns array as the value of this element-value.
     * @return array as the value
     */
    public ArrayValue getArrayValue() {
        return arrayValue;
    }
}