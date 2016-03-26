package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class StackMapFrameBuilder {
	/**
	 * 
	 */
	private static StackMapFrameBuilder builder = null;

	/**
	 * 
	 * @return 
	 */
	public static StackMapFrameBuilder getInstance() {
		if(builder == null) {
			builder = new StackMapFrameBuilder();
		}
		return builder;
 	}

	public StackMapFrame build(RandomAccessFile raf)
	throws IOException, StackMapFrameException {
		int tag = raf.readUnsignedByte();
		if(0 <= tag && tag <= 63) {
			return new SameFrame(StackMapFrameType.SameFrame, tag);
		} else if(64 <= tag && tag <= 127) {
			return new SameLocals1StackItemFrame(tag, raf);
		} else if(128 <= tag && tag <= 246) {
			throw new StackMapFrameException(" the range of [128-246] is reserved for future use.");
		} else if(tag == 247) {
			return new SameLocals1StackItemFrameExtended(tag, raf);
		} else if(248 <= tag && tag <= 250) {
			return new ChopFrame(StackMapFrameType.ChopFrame, tag, raf);
		} else if(tag == 251) {
			return new SameFrameExtended(StackMapFrameType.SameFrameExtended, tag, raf);
		} else if(tag <= 252 && tag <= 254) {
			return new AppendFrame(tag, raf);
		} else if(tag == 255) {
			return new FullFrame(tag, raf);
		} else {
			throw new StackMapFrameException(tag);
		}
	}
}
