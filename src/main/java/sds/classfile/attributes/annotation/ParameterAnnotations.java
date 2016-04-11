package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class ParameterAnnotations {
	/**
	 *
	 */
	Annotation[] annotations;

	/**
	 *
	 * @param raf
	 * @throws IOException
	 */
	ParameterAnnotations(RandomAccessFile raf) throws IOException, ElementValueException {
		this.annotations = new Annotation[raf.readByte()];
		for(int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation(raf);
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}
}