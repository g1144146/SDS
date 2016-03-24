package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class SourceFile extends AttributeInfo {
	/**
	 * 
	 */
	int sourceFileIndex;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public SourceFile(int nameIndex, int length) {
		super(AttributeType.SourceFile, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public int getSourceFileIndex() {
		return sourceFileIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.sourceFileIndex = raf.readShort();
	}
}
