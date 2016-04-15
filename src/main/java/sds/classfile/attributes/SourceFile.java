package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.10">SourceFile Attribute</a>.
 * @author inagaki
 */
public class SourceFile extends AttributeInfo {
	private int sourceFileIndex;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public SourceFile(int nameIndex, int length) {
		super(AttributeType.SourceFile, nameIndex, length);
	}

	/**
	 * returns constant-pool entry index of source file.
	 * @return constant-pool entry index of source file
	 */
	public int getSourceFileIndex() {
		return sourceFileIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.sourceFileIndex = raf.readShort();
	}
}