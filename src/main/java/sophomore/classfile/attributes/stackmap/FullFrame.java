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

	public FullFrame(RandomAccessFile raf) throws IOException {
		super(raf);
		int numberOfLocals = raf.readShort();
		this.locals = new VerificationTypeInfo[numberOfLocals];
		for(int i = 0; i < numberOfLocals; i++) {
			locals[i] = new VerificationTypeInfo(raf);
		}
		int numberOfStackItems = raf.readShort();
		this.stack = new VerificationTypeInfo[numberOfStackItems];
		for(int i = 0; i < numberOfStackItems; i++) {
			stack[i] = new VerificationTypeInfo(raf);
		}
	}

	public VerificationTypeInfo[] getLocals() {
		return locals;
	}

	public VerificationTypeInfo[] getStack() {
		return stack;
	}
}
