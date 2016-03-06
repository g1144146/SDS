package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.AttributeType;

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
	RuntimeTypeAnnotations(AttributeType.Type type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.annotations = new TypeAnnotation[len];
		for(int i = 0; i < len; i++) {
			annotations[i] = new TypeAnnotation(raf);
		}
	}

	public TypeAnnotation[] getAnnotatinos() {
		return annotations;
	}
}
