package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This builder class is for {@link StackMapFrame <code>StackMapFrame</code>}.<br>
 * This class is designed singleton.
 * @author inagaki
 */
public class StackMapFrameBuilder {
	private static StackMapFrameBuilder builder = null;

	/**
	 * returns own instance.
	 * @return instance
	 */
	public static StackMapFrameBuilder getInstance() {
		if(builder == null) {
			builder = new StackMapFrameBuilder();
		}
		return builder;
 	}

	/**
	 * returns stack-map-frame.
	 * @param data classfile stream
	 * @return stack-map-frame
	 * @throws IOException
	 * @throws StackMapFrameException 
	 */
	public StackMapFrame build(ClassFileStream data)
	throws IOException, StackMapFrameException {
		int tag = data.readUnsignedByte();
		if(0 <= tag && tag <= 63) {
			return new SameFrame(StackMapFrameType.SameFrame, tag);
		} else if(64 <= tag && tag <= 127) {
			return new SameLocals1StackItemFrame(tag, data);
		} else if(128 <= tag && tag <= 246) {
			throw new StackMapFrameException(" the range of [128-246] is reserved for future use.");
		} else if(tag == 247) {
			return new SameLocals1StackItemFrameExtended(tag, data);
		} else if(248 <= tag && tag <= 250) {
			return new ChopFrame(StackMapFrameType.ChopFrame, tag, data);
		} else if(tag == 251) {
			return new SameFrameExtended(StackMapFrameType.SameFrameExtended, tag, data);
		} else if(252 <= tag && tag <= 254) {
			return new AppendFrame(tag, data);
		} else if(tag == 255) {
			return new FullFrame(tag, data);
		} else {
			throw new StackMapFrameException(tag);
		}
	}
}