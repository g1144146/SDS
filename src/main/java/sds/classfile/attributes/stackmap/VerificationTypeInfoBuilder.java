package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This builder class is for
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.<br>
 * This class is designed singleton.
 * @author inagaki
 */
public class VerificationTypeInfoBuilder {
	private static VerificationTypeInfoBuilder builder = null;

	/**
	 * returns own instance.
	 * @return instance
	 */
	public static VerificationTypeInfoBuilder getInstance() {
		if(builder == null) {
			builder = new VerificationTypeInfoBuilder();
		}
		return builder;
	}

	/**
	 * returns verification type info.
	 * @param raf classfile stream
	 * @return verification type info
	 * @throws IOException
	 * @throws VerificationTypeException 
	 */
	public VerificationTypeInfo build(RandomAccessFile raf)
	throws IOException, VerificationTypeException {
		int tag = raf.readUnsignedByte();
		switch(tag) {
			case 0: return new TopVariableInfo(tag);
			case 1: return new IntegerVariableInfo(tag);
			case 2: return new FloatVariableInfo(tag);
			case 3: return new LongVariableInfo(tag);
			case 4: return new DoubleVariableInfo(tag);
			case 5: return new NullVariableInfo(tag);
			case 6: return new UninitializedThisVariableInfo(tag);
			case 7: return new ObjectVariableInfo(tag, raf.readShort());
			case 8: return new UninitializedVariableInfo(tag, raf.readShort());
			default: throw new VerificationTypeException(tag);
		}
	}
}