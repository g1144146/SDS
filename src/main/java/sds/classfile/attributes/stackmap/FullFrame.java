package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

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
	 * @param data classfile stream
	 * @throws IOException 
	 * @throws sds.classfile.attributes.stackmap.VerificationTypeException 
	 */
	public FullFrame(int tag, ClassFileStream data) throws IOException, VerificationTypeException {
		super(StackMapFrameType.FullFrame, tag, data);
		VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();
		this.locals = new VerificationTypeInfo[data.readShort()];
		for(int i = 0; i < locals.length; i++) {
			locals[i] = builder.build(data);
		}
		this.stack = new VerificationTypeInfo[data.readShort()];
		for(int i = 0; i < stack.length; i++) {
			stack[i] = builder.build(data);
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