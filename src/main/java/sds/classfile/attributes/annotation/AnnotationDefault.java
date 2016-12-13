package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

import static sds.util.AnnotationParser.parseElementValue;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.22">AnnotationDefault Attribute</a>.
 * @author inagaki
 */
public class AnnotationDefault extends AttributeInfo {
	private String defaultValue;

	/**
	 * constructor.
	 */
	public AnnotationDefault() {
		super(AttributeType.AnnotationDefault);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		try {
			ElementValue value = new ElementValue(data);
			this.defaultValue = parseElementValue(value, new StringBuilder(), pool);
		} catch(ElementValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns default value of the annotation type element.
	 * @return default value of the annotation type element
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
}