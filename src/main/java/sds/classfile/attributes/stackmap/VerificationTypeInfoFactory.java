package sds.classfile.attributes.stackmap;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This factory class is for
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class VerificationTypeInfoFactory {
	/**
	 * returns verification type info.
	 * @param data classfile stream
	 * @return verification type info
	 * @throws IOException
	 * @throws VerificationTypeException 
	 */
	public VerificationTypeInfo create(ClassFileStream data)
	throws IOException, VerificationTypeException {
		int tag = data.readUnsignedByte();
		switch(tag) {
			case 0: return new TopVariableInfo(tag);
			case 1: return new IntegerVariableInfo(tag);
			case 2: return new FloatVariableInfo(tag);
			case 3: return new LongVariableInfo(tag);
			case 4: return new DoubleVariableInfo(tag);
			case 5: return new NullVariableInfo(tag);
			case 6: return new UninitializedThisVariableInfo(tag);
			case 7: return new ObjectVariableInfo(tag, data.readShort());
			case 8: return new UninitializedVariableInfo(tag, data.readShort());
			default: throw new VerificationTypeException(tag);
		}
	}
}