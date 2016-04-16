package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.11">
 * SourceDebugExtension Attribute</a>.
 * @author inagakikenichi
 */
public class SourceDebugExtension extends AttributeInfo {
	private int[] debugExtension;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public SourceDebugExtension(int nameIndex, int length) {
		super(AttributeType.SourceDebugExtension, nameIndex, length);
	}

	/**
	 * returns constant-pool entry index of debugging information.
	 * @return constant-pool entry index of debugging information
	 */
	public int[] getDebugExtension() {
		return debugExtension;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.debugExtension = new int[this.getAttrLen()];
		for(int i = 0; i < this.getAttrLen(); i++) {
			debugExtension[i] = raf.readUnsignedByte();
		}
	}
}