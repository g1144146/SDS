package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class SameLocals1StackItemFrame extends SameFrame {
	/**
	 *
	 */
	VerificationTypeInfo stack;

	SameLocals1StackItemFrame(int tag, RandomAccessFile raf) throws IOException {
		super(StackMapFrameType.SameLocals1StackItemFrame, tag);
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();			
			this.stack = builder.build(raf);
		} catch(VerificationTypeException e) {
			e.printStackTrace();
		}
	}

	public VerificationTypeInfo getStack() {
		return stack;
	}
}
