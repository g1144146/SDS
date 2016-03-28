package sophomore.classfile.attributes.stackmap;

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
