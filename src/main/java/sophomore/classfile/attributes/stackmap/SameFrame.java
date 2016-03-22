package sophomore.classfile.attributes.stackmap;

/**
 *
 * @author inagaki
 */
public class SameFrame {
	/**
	 * 
	 */
	int frameType;

	SameFrame(int frameType) {
		this.frameType = frameType;
	}

	public int getFrameType() {
		return frameType;
	}
}
