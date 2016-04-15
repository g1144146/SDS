package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 * This adapter class is for
 * {@link RuntimeVisibleAnnotations <code>RuntimeVisibleAnnotations</code>} and 
 * {@link RuntimeInvisibleAnnotations <code>RuntimeInvisibleAnnotations</code>}.
 * @author inagaki
 */
abstract class RuntimeAnnotations extends AttributeInfo {
	private Annotation[] annotations;

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

	/**
	 * returns runtime annotations.
	 * @return runtime annotations
	 */
	public Annotation[] getAnnotations() {
		return annotations;
	}
}