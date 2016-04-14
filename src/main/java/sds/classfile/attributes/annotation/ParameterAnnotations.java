package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeVisibleParameterAnnotations <code>RuntimeVisibleParameterAnnotations</code>}
 * and 
 * {@link RuntimeInvisibleParameterAnnotations <code>RuntimeInvisibleParameterAnnotations</code>}
 * have item.
 * @author inagaki
 */
public class ParameterAnnotations {
	/**
	 * runtime parameter annotations.
	 */
	Annotation[] annotations;

	/**
	 * constructor.
	 * @param raf classfile stream
	 * @throws IOException
	 */
	ParameterAnnotations(RandomAccessFile raf) throws IOException, ElementValueException {
		this.annotations = new Annotation[raf.readByte()];
		for(int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation(raf);
		}
	}

	/**
	 * returns runtime parameter annotations.
	 * @return runtime parameter annotations
	 */
	public Annotation[] getAnnotations() {
		return annotations;
	}
}