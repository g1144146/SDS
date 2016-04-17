package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for same_frame_extended which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class SameFrameExtended extends ChopFrame {
	/**
	 * constructor.
	 * @param frameType stack-map-frame type
	 * @param tag discrimination tag
	 * @param raf classfile stream
	 * @throws IOException 
	 */
	public SameFrameExtended(StackMapFrameType frameType, int tag, RandomAccessFile raf)
	throws IOException {
		super(frameType, tag, raf);
	}
}