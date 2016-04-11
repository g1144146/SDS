package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 *
 * @author inagakikenichi
 */
abstract class RuntimeParameterAnnotations extends AttributeInfo {
	/**
	 *
	 */
	ParameterAnnotations[] parameterAnnotations;

	/**
	 *
	 * @param type
	 */
	RuntimeParameterAnnotations(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.parameterAnnotations = new ParameterAnnotations[raf.readByte()];
		try {
			for(int i = 0; i < parameterAnnotations.length; i++) {
				parameterAnnotations[i] = new ParameterAnnotations(raf);
			}
		} catch(ElementValueException e) {
			e.printStackTrace();
		}
	}

	public ParameterAnnotations[] getParamAnnotations() {
		return parameterAnnotations;
	}
}