package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class ObjectVariableInfo {
	int tag;
	int cpoolIndex;
	ObjectVariableInfo(RandomAccessFile raf) throws IOException {
		this.tag = raf.readByte();
		this.cpoolIndex = raf.readShort();
	}

	public int getTag() {
		return tag;
	}

	public int getCPoolIndex() {
		return cpoolIndex;
	}
}
