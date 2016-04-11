package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class ChopFrame extends SameFrame {
	/**
	 *
	 */
	int offsetDelta;

	ChopFrame(StackMapFrameType type, int tag, RandomAccessFile raf) throws IOException {
		super(type, tag);
		this.offsetDelta = raf.readShort();
	}

	public int getOffset() {
		return offsetDelta;
	}
}
