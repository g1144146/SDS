package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

import static sds.util.AnnotationParser.parseAnnotation;

/**
 * This adapter class is for RuntimeTypeAnnotations Attribute.
 * @author inagaki
 */
abstract class RuntimeTypeAnnotations extends AttributeInfo {
	private TypeAnnotation[] types;
	private String[] annotations;

	RuntimeTypeAnnotations(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool)
	throws IOException, ElementValueException, TargetTypeException {
		this.types = new TypeAnnotation[data.readShort()];
		annotations = new String[types.length];
		for(int i = 0; i < types.length; i++) {
			types[i] = new TypeAnnotation(data);
			annotations[i] = parseAnnotation(types[i], new StringBuilder(), pool);
		}
	}

	/**
	 * returns runtime type annotation strings.
	 * @return runtime type annotation strings
	 */
	public String[] getAnnotations() {
		return annotations;
	}

	/**
	 * returns runtime type annotations.
	 * @return runtime type annotations
	 */
	public TypeAnnotation[] getTypes() {
		return types;
	}
}