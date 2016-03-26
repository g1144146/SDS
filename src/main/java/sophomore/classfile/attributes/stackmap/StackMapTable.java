package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.AttributeType;

/**
 *
 * @author inagaki
 */
public class StackMapTable extends AttributeInfo {
	/**
	 *
	 */
	 StackMapFrame[] entries;
	/**
	 *
	 * @param nameIndex
	 * @param length
	 */
	public StackMapTable(int nameIndex, int length) {
		super(AttributeType.StackMapTable, nameIndex, length);
	}

	/**
	 *
	 * @return
	 */
	public StackMapFrame[] getEntries() {
		return entries;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.entries = new StackMapFrame[len];
		try {
			StackMapFrameBuilder builder = StackMapFrameBuilder.getInstance();
			for(int i = 0; i < len; i++) {
				entries[i] = builder.build(raf);
			}
		} catch(StackMapFrameException e) {
			e.printStackTrace();
		}
	}
}
