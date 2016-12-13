package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.8">Synthetic Attribute</a>.
 * @author inagaki
 */
public class Synthetic extends AttributeInfo {
	/**
	 * constructor.
	 */
	public Synthetic() {
		super(AttributeType.Synthetic);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		// do nothing.
	}
}