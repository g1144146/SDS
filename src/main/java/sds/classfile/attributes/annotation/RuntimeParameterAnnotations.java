package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 * This adapter class is for RuntimeParameterAnnotations Attribute.
 * @author inagaki
 */
abstract class RuntimeParameterAnnotations extends AttributeInfo {
	/**
	 * runtime parameter annotations.
	 */
	ParameterAnnotations[] parameterAnnotations;

	/**
	 * constructor.
	 * @param type attribute type
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
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

	/**
	 * returns runtime parameter annotations.
	 * @return runtime parameter annotations
	 */
	public ParameterAnnotations[] getParamAnnotations() {
		return parameterAnnotations;
	}
}