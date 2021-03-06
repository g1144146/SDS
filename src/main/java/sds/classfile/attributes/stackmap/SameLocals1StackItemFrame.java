package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for same_locals_1_stack_item_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class SameLocals1StackItemFrame extends SameFrame {
	private VerificationTypeInfo stack;

	SameLocals1StackItemFrame(int tag, ClassFileStream data) throws IOException, VerificationTypeException {
		super(StackMapFrameType.SameLocals1StackItemFrame, tag);
		VerificationTypeInfoFactory factory = new VerificationTypeInfoFactory();
		this.stack = factory.create(data);
	}

	/**
	 * returns verification type info.
	 * @return verification type info
	 */
	public VerificationTypeInfo getStack() {
		return stack;
	}
}