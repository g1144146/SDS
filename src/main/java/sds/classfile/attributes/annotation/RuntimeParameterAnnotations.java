package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 * This adapter class is for RuntimeParameterAnnotations Attribute.
 * @author inagaki
 */
abstract class RuntimeParameterAnnotations extends AttributeInfo {
	private ParameterAnnotations[] parameterAnnotations;

	RuntimeParameterAnnotations(AttributeType type) {
		super(type);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException, ElementValueException {
		this.parameterAnnotations = new ParameterAnnotations[data.readByte()];
		for(int i = 0; i < parameterAnnotations.length; i++) {
			parameterAnnotations[i] = new ParameterAnnotations(data, pool);
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