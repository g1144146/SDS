package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class ElementValue {
	/**
	 *
	 */
	int tag;
	/**
	 *
	 */
	int constValueIndex;
	/**
	 *
	 */
	EnumConstValue enumConstValue;
	/**
	 *
	 */
	int classInfoIndex;
	/**
	 *
	 */
	Annotation annotationValue;
	/**
	 *
	 */
	ArrayValue arrayValue;
	
	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	ElementValue(RandomAccessFile raf) throws IOException, ElementValueException {
		this.tag = raf.readByte();
		switch((char)tag) {
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
	public int getTag() {
		return tag;
	}
	public int getConstValueIndex() {
		return constValueIndex;
	}
	public EnumConstValue getEnumConstValue() {
		return enumConstValue;
	}
	public int getClassInfoIndex() {
		return classInfoIndex;
	}
	public Annotation getAnnotationValue() {
		return annotationValue;
	}
	public ArrayValue getArrayValue() {
		return arrayValue;
	}
}