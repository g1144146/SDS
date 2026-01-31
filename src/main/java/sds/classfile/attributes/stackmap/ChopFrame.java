package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for chop_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class ChopFrame extends SameFrame {
	private int offsetDelta;

	/**
	 * Constructs a ChopFrame with the specified type, tag, and classfile stream.
	 * @param type stack-map-frame type
	 * @param tag discrimination tag
	 * @param data classfile stream
	 * @throws IOException if an error occurs during reading
	 */
	ChopFrame(StackMapFrameType type, int tag, ClassFileStream data) throws IOException {
		super(type, tag);
		this.offsetDelta = data.readShort();
	}

	/**
	 * returns offset.
	 * @return offset
	 */
	public int getOffset() {
		return offsetDelta;
	}
}
