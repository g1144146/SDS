package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
abstract class RuntimeTypeAnnotations extends AttributeInfo {

	/**
	 * 
	 */
	TypeAnnotation[] annotations;

	/**
	 * 
	 * @param type
	 * @param nameIndex
	 * @param length 
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

	public TypeAnnotation[] getAnnotations() {
		return annotations;
	}
}
