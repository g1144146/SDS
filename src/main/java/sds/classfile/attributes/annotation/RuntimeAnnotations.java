package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

import static sds.util.AnnotationParser.parseAnnotation;

/**
 * This adapter class is for
 * {@link RuntimeVisibleAnnotations <code>RuntimeVisibleAnnotations</code>} and 
 * {@link RuntimeInvisibleAnnotations <code>RuntimeInvisibleAnnotations</code>}.
 * @author inagaki
 */
abstract class RuntimeAnnotations extends AttributeInfo {
	private String[] annotations;

	RuntimeAnnotations(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.annotations = new String[data.readShort()];
		try {
			for(int i = 0; i < annotations.length; i++) {
				annotations[i] = parseAnnotation(new Annotation(data), new StringBuilder(), pool);
			}
		} catch(ElementValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns runtime annotations.
	 * @return runtime annotations
	 */
	public String[] getAnnotations() {
		return annotations;
	}
}