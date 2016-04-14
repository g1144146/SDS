package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 * This adapter class is for RuntimeTypeAnnotations Attribute.
 * @author inagaki
 */
abstract class RuntimeTypeAnnotations extends AttributeInfo {
	/**
	 * runtime type annotations.
	 */
	TypeAnnotation[] annotations;

	/**
	 * constructor.
	 * @param type attribute type
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	RuntimeTypeAnnotations(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.annotations = new TypeAnnotation[raf.readShort()];
		try {
			for(int i = 0; i < annotations.length; i++) {
				annotations[i] = new TypeAnnotation(raf);
			}
		} catch(ElementValueException | TargetTypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns runtime type annotations.
	 * @return runtime type annotations
	 */
	public TypeAnnotation[] getAnnotations() {
		return annotations;
	}
}