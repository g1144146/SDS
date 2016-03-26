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

	public AppendFrame(int tag, RandomAccessFile raf)
	throws IOException {
		super(StackMapFrameType.AppendFrame, tag, raf);
		this.locals = new VerificationTypeInfo[tag - 251];
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();
			for(int i = 0; i < locals.length; i++) {
				locals[i] = builder.build(raf);
			}
		} catch(VerificationTypeException e) {
			e.printStackTrace();
		}
	}

	public VerificationTypeInfo[] getLocals() {
		return locals;
	}
}
