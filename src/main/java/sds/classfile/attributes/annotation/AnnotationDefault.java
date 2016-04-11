package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class AnnotationDefault extends AttributeInfo {
	/**
	 * 
	 */
	ElementValue defaultValue;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
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

	public ElementValue getDefaultValue() {
		return defaultValue;
	}
}
