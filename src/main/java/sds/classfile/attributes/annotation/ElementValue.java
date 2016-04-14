package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for single element-value in the annotation.
 * @author inagaki
 */
public class ElementValue {
	/**
	 *  ASCII character to indicate the type of the value of the element-value.
	 */
	char tag;
	/**
	 * primitive constant value or a String literal as the value of this element-value.
	 */
	int constValueIndex;
	/**
	 * enum constant as the value of this element-value.
	 */
	EnumConstValue enumConstValue;
	/**
	 * constant-pool entry index of class.
	 */
	int classInfoIndex;
	/**
	 * nested annotation.
	 */
	Annotation annotationValue;
	/**
	 * array as the value of this element-value.
	 */
	ArrayValue arrayValue;
	
	/**
	 * constructor.
	 * @param raf classfile stream
	 * @throws IOException
	 */
	ElementValue(RandomAccessFile raf) throws IOException, ElementValueException {
		this.tag = (char)raf.readByte();
		switch(tag) {
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
			case 's': this.constValueIndex = raf.readShort();         break;
			case 'e': this.enumConstValue  = new EnumConstValue(raf); break;
			case 'c': this.classInfoIndex  = raf.readShort();         break;
			case '@': this.annotationValue = new Annotation(raf);     break;
			case '[': this.arrayValue      = new ArrayValue(raf);     break;
			default:  throw new ElementValueException(tag);
		}
	}

	/**
	 * returns ASCII character to indicate the type of the value of the element-value.
	 * @return ASCII character
	 */
	public char getTag() {
		return tag;
	}

	/**
	 * returns primitive constant value or a String literal as the value of this element-value.
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