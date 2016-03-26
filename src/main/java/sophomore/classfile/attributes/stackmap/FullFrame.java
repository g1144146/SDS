package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class FullFrame extends ChopFrame {
	/**
	 * 
	 */
	VerificationTypeInfo[] locals;
	/**
	 * 
	 */
	VerificationTypeInfo[] stack;

	public FullFrame(int tag, RandomAccessFile raf)
	throws IOException {
		super(StackMapFrameType.FullFrame, tag, raf);
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();
			int localsLen = raf.readShort();
			this.locals = new VerificationTypeInfo[localsLen];
			for(int i = 0; i < localsLen; i++) {
				locals[i] = builder.build(raf);
			}
			int stackLen = raf.readShort();
			this.stack = new VerificationTypeInfo[stackLen];
			for(int i = 0; i < stackLen; i++) {
				stack[i] = builder.build(raf);
			}
		} catch(VerificationTypeException e) {
			e.printStackTrace();
		}
	}

	public VerificationTypeInfo[] getLocals() {
		return locals;
	}

	public VerificationTypeInfo[] getStack() {
		return stack;
	}
}
