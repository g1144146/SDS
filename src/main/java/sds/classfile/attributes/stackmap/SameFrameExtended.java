package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

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
	 * @param data classfile stream
	 * @throws IOException 
	 */
	public SameFrameExtended(StackMapFrameType frameType, int tag, ClassFileStream data)
	throws IOException {
		super(frameType, tag, data);
	}
}