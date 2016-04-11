package sds.classfile.attributes.stackmap;

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

	public FullFrame(int tag, RandomAccessFile raf) throws IOException {
		super(StackMapFrameType.FullFrame, tag, raf);
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();
			this.locals = new VerificationTypeInfo[raf.readShort()];
			for(int i = 0; i < locals.length; i++) {
				locals[i] = builder.build(raf);
			}
			this.stack = new VerificationTypeInfo[raf.readShort()];
			for(int i = 0; i < stack.length; i++) {
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
