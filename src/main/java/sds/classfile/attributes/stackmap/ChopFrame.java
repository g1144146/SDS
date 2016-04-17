package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for chop_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class ChopFrame extends SameFrame {
	private int offsetDelta;

	ChopFrame(StackMapFrameType type, int tag, RandomAccessFile raf) throws IOException {
		super(type, tag);
		this.offsetDelta = raf.readShort();
	}

	/**
	 * returns offset.
	 * @return offset
	 */
	public int getOffset() {
		return offsetDelta;
	}
}