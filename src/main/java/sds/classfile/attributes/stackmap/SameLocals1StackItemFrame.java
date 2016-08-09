package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;
import sds.classfile.ClassFileStream;

/**
 * This class is for same_locals_1_stack_item_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class SameLocals1StackItemFrame extends SameFrame {
	private VerificationTypeInfo stack;

	SameLocals1StackItemFrame(int tag, ClassFileStream data) throws IOException {
		super(StackMapFrameType.SameLocals1StackItemFrame, tag);
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();			
			this.stack = builder.build(data);
		} catch(VerificationTypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns verification type info.
	 * @return verification type info
	 */
	public VerificationTypeInfo getStack() {
		return stack;
	}
}