package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.AttributeType;

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
		super(AttributeType.Type.AnnotationDefault, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.defaultValue = new ElementValue(raf);
	}

	public ElementValue getDefaultValue() {
		return defaultValue;
	}
}
