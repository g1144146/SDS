/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class SameLocals1StackItemFrameExtended extends SameFrame {
	/**
	 *
	 */
	int offsetDelta;
	/**
	 *
	 */
	VerificationTypeInfo stack;

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

	public int getOffset() {
		return offsetDelta;
	}

	public VerificationTypeInfo getStack() {
		return stack;
	}
}
