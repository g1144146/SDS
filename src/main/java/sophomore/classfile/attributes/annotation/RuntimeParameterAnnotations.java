package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.AttributeType;

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
	RuntimeParameterAnnotations(AttributeType.Type type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readByte();
		this.parameterAnnotations = new ParameterAnnotations[len];
		for(int i = 0; i < len; i++) {
			parameterAnnotations[i] = new ParameterAnnotations(raf);
		}
	}

	public ParameterAnnotations[] getParamAnnotations() {
		return parameterAnnotations;
	}
}