package sds.classfile.attributes.stackmap;

/**
 * This class is for same_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class SameFrame implements StackMapFrame {
	private StackMapFrameType frameType;
	private int tag;

	SameFrame(StackMapFrameType frameType, int tag) {
		this.frameType = frameType;
		this.tag = tag;
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