package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for same_locals_stack_item_frame_extended which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagakikenichi
 */
public class SameLocals1StackItemFrameExtended extends SameFrame {
	private int offsetDelta;
	private VerificationTypeInfo stack;

	SameLocals1StackItemFrameExtended(int tag, ClassFileStream data)
	throws IOException, VerificationTypeException {
		super(StackMapFrameType.SameLocals1StackItemFrameExtended, tag);
		this.offsetDelta = data.readShort();
		VerificationTypeInfoFactory factory = new VerificationTypeInfoFactory();
		this.stack = factory.create(data);
	}

	/**
	 * returns offset.
	 * @return offset
	 */
	public int getOffset() {
		return offsetDelta;
	}

	/**
	 * returns verification type info.
	 * @return verification type info
	 */
	public VerificationTypeInfo getStack() {
		return stack;
	}
}