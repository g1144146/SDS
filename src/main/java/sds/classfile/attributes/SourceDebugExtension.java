package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.11">
 * SourceDebugExtension Attribute</a>.
 * @author inagakikenichi
 */
public class SourceDebugExtension extends AttributeInfo {
	private int[] debugExtension;
	private int attrLen;

	/**
	 * constructor.
	 * @param length attribute length
	 */
	public SourceDebugExtension(int length) {
		super(AttributeType.SourceDebugExtension);
		this.attrLen = length;
	}

	/**
	 * returns constant-pool entry index of debugging information.
	 * @return constant-pool entry index of debugging information
	 */
	public int[] getDebugExtension() {
		return debugExtension;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.debugExtension = new int[attrLen];
		for(int i = 0; i < debugExtension.length; i++) {
			debugExtension[i] = data.readUnsignedByte();
		}
	}
}