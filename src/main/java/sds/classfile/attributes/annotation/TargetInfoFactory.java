package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This factory class is for {@link TargetInfo <code>TargetInfo</code>}.
 * @author inagaki
 */
public class TargetInfoFactory {
	/**
	 * returns target info.
	 * @param data classfile stream
	 * @return target info
	 * @throws IOException
	 * @throws TargetTypeException 
	 */
	public TargetInfo create(ClassFileStream data) throws IOException, TargetTypeException {
		int targetType = data.readUnsignedByte();
		switch(targetType) {
			case 0x00:
			case 0x01: return new TypeParameterTarget(data.readUnsignedByte());
			case 0x10: return new SuperTypeTarget(data.readShort());
			case 0x11:
			case 0x12: return new TypeParameterBoundTarget(data.readUnsignedByte(), data.readUnsignedByte());
			case 0x13:
			case 0x14:
			case 0x15: return new EmptyTarget();
			case 0x16: return new MethodFormalParameterTarget(data.readUnsignedByte());
			case 0x17: return new ThrowsTarget(data.readShort());
			case 0x40:
			case 0x41: return new LocalVarTarget(data);
			case 0x42: return new CatchTarget(data.readShort());
			case 0x43:
			case 0x44:
			case 0x45:
			case 0x46: return new OffsetTarget(data.readShort());
			case 0x47:
			case 0x48:
			case 0x49:
			case 0x4A:
			case 0x4B: return new TypeArgumentTarget(data.readShort(), data.readUnsignedByte());
			default:   throw  new TargetTypeException(targetType);
		}
	}
}