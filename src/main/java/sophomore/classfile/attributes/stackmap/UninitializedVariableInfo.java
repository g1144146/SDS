package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class UninitializedVariableInfo {
	int tag;
	int offset;
	UninitializedVariableInfo(RandomAccessFile raf) throws IOException {
		this.tag = raf.readByte();
		this.offset = raf.readShort();
	}

	public int getTag() {
		return tag;
	}

	public int getOffset() {
		return offset;
	}
}
