package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class AppendFrame extends ChopFrame {
	/**
	 * 
	 */
	VerificationTypeInfo[] locals;

	public AppendFrame(RandomAccessFile raf) throws IOException {
		super(raf);
		this.locals = new VerificationTypeInfo[frameType - 251];
		for(int i = 0; i < locals.length; i++) {
			locals[i] = new VerificationTypeInfo(raf);
		}
	}

	public VerificationTypeInfo[] getLocals() {
		return locals;
	}
}
