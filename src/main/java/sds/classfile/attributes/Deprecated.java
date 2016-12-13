package sds.classfile.attributes;

import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.15">
 * Deprecated Attribute</a>.
 * @author inagaki
 */
public class Deprecated extends AttributeInfo {
	/**
	 * constructor.
	 */
	public Deprecated() {
		super(AttributeType.Deprecated);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws Exception {
		// do nothing.
	}
}