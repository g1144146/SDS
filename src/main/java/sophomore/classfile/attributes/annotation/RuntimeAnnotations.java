package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.AttributeType;

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
		int len = raf.readShort();
		this.annotations = new Annotation[len];
		for(int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation(raf);
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}
}
