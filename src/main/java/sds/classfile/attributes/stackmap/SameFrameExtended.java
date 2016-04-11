package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class SameFrameExtended extends ChopFrame {
	public SameFrameExtended(StackMapFrameType frameType, int tag, RandomAccessFile raf)
	throws IOException {
		super(frameType, tag, raf);
	}
}
