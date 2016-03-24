package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class SameLocalsStackItemFrame {
	/**
	 * 
	 */
	int frameType;
	/**
	 * 
	 */
	VerificationTypeInfo stack;

	SameLocalsStackItemFrame(RandomAccessFile raf) throws IOException {
		this.frameType = raf.readByte();
		this.stack = new VerificationTypeInfo(raf);
	}

	public int getFrameType() {
		return frameType;
	}

	public VerificationTypeInfo getStack() {
		return stack;
	}
}
