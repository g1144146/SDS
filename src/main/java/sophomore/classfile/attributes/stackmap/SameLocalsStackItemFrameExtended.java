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
public class SameLocalsStackItemFrameExtended {
	/**
	 * 
	 */
	int frameType;
	/**
	 * 
	 */
	int offsetDelta;
	/**
	 * 
	 */
	VerificationTypeInfo stack;

	SameLocalsStackItemFrameExtended(RandomAccessFile raf) throws IOException {
		this.frameType = raf.readByte();
		this.offsetDelta = raf.readShort();
		this.stack = new VerificationTypeInfo(raf);
	}

	public int getFrameType() {
		return frameType;
	}

	public int getOffset() {
		return offsetDelta;
	}

	public VerificationTypeInfo getStack() {
		return stack;
	}
}
