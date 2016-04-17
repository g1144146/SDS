package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for full_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class FullFrame extends ChopFrame {
	private VerificationTypeInfo[] locals;
	private VerificationTypeInfo[] stack;

	/**
	 * constructor.
	 * @param tag discrimination tag
	 * @param raf classfile stream
	 * @throws IOException 
	 */
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

	/**
	 * returns verification type info of locals.
	 * @return verification type info
	 */
	public VerificationTypeInfo[] getLocals() {
		return locals;
	}

	/**
	 * returns verification type info of stack.
	 * @return verificatino type info
	 */
	public VerificationTypeInfo[] getStack() {
		return stack;
	}
}