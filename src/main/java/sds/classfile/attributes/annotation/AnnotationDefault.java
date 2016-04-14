package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.22">AnnotationDefault Attribute</a>.
 * @author inagaki
 */
public class AnnotationDefault extends AttributeInfo {
	/**
	 * default value of the annotation type element.
	 */
	ElementValue defaultValue;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public AnnotationDefault(int nameIndex, int length) {
		super(AttributeType.AnnotationDefault, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		try {
			this.defaultValue = new ElementValue(raf);
		} catch(ElementValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns default value of the annotation type element.
	 * @return default value of the annotation type element
	 */
	public ElementValue getDefaultValue() {
		return defaultValue;
	}
}