package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class ParameterAnnotations {
	/**
	 *
	 */
	Annotation[] annotations;

	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	ParameterAnnotations(RandomAccessFile raf) throws IOException {
		int len= raf.readByte();
		this.annotations = new Annotation[len];
		for(int i = 0; i < len; i++) {
			annotations[i] = new Annotation(raf);
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}
}