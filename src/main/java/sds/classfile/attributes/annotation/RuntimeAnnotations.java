package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagakikenichi
 */
abstract class RuntimeAnnotations extends AttributeInfo {
	/**
	 * 
	 */
	Annotation[] annotations;

	/**
	 * 
	 * @param type
	 * @param nameIndex
	 * @param length 
	 */
	RuntimeAnnotations(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.annotations = new Annotation[raf.readShort()];
		try {
			for(int i = 0; i < annotations.length; i++) {
				annotations[i] = new Annotation(raf);
			}
		} catch(ElementValueException e) {
			e.printStackTrace();
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}
}
