package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class SameFrame implements StackMapFrame {
	/**
	 *
	 */
	StackMapFrameType frameType;
	/**
	 * 
	 */
	int tag;

	SameFrame(StackMapFrameType frameType, int tag) {
		this.frameType = frameType;
		this.tag = tag;
		System.out.println("sameframe: "+frameType);
	}

	@Override
	public StackMapFrameType getFrameType() {
		return frameType;
	}

	@Override
	public int getTag() {
		return tag;
	}
}
