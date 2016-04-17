package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for same_locals_stack_item_frame_extended which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagakikenichi
 */
public class SameLocals1StackItemFrameExtended extends SameFrame {
	private int offsetDelta;
	private VerificationTypeInfo stack;

	SameLocals1StackItemFrameExtended(int tag, RandomAccessFile raf) throws IOException {
		super(StackMapFrameType.SameLocals1StackItemFrameExtended, tag);
		this.offsetDelta = raf.readShort();
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();			
			this.stack = builder.build(raf);
		} catch(VerificationTypeException e) {
			e.printStackTrace();
		}
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