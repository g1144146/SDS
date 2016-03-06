package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class ElementValue {
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
	ElementValue(RandomAccessFile raf) throws IOException {
		this.tag = raf.readByte();
		this.constValueIndex = raf.readShort();
		this.enumConstValue = new EnumConstValue(raf);
		this.classInfoIndex = raf.readShort();
		this.annotationValue = new Annotation(raf);
		this.arrayValue = new ArrayValue(raf);
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