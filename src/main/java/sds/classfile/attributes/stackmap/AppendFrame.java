package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for append_frame which
 * {@link StackMapFrame <code>SrackMapFrame</code>} has.
 * @author inagaki
 */
public class AppendFrame extends ChopFrame {
	private VerificationTypeInfo[] locals;

	public AppendFrame(int tag, RandomAccessFile raf) throws IOException {
		super(StackMapFrameType.AppendFrame, tag, raf);
		this.locals = new VerificationTypeInfo[tag - 251];
		try {
			VerificationTypeInfoBuilder builder = VerificationTypeInfoBuilder.getInstance();
			for(int i = 0; i < locals.length; i++) {
				locals[i] = builder.build(raf);
			}
		} catch(VerificationTypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns verification type info.
	 * @return verification type info
	 */
	public VerificationTypeInfo[] getLocals() {
		return locals;
	}
}