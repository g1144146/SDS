package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This builder class is for {@link TargetInfo <code>TargetInfo</code>}.<br>
 * This class is designed singleton.
 * @author inagaki
 */
public class TargetInfoBuilder {
	/**
	 * instance of builder class.
	 */
	private static TargetInfoBuilder builder = null;

	/**
	 * returns own instance.
	 * @return instance
	 */
	public static TargetInfoBuilder getInstance() {
		if(builder == null) {
			builder = new TargetInfoBuilder();
		}
		return builder;
	}

	/**
	 * returns target info.
	 * @param raf classfile stream
	 * @return target info
	 * @throws IOException
	 * @throws TargetTypeException 
	 */
	public TargetInfo build(RandomAccessFile raf) throws IOException, TargetTypeException {
		int targetType = raf.readUnsignedByte();
		switch(targetType) {
			case 0x00:
			case 0x01: return new TypeParameterTarget(raf.readUnsignedByte());
			case 0x10: return new SuperTypeTarget(raf.readShort());
			case 0x11:
			case 0x12: return new TypeParameterBoundTarget(raf.readUnsignedByte(), raf.readUnsignedByte());
			case 0x13:
			case 0x14:
			case 0x15: return new EmptyTarget();
			case 0x16: return new MethodFormalParameterTarget(raf.readUnsignedByte());
			case 0x17: return new ThrowsTarget(raf.readShort());
			case 0x40:
			case 0x41: return new LocalVarTarget(raf);
			case 0x42: return new CatchTarget(raf.readShort());
			case 0x43:
			case 0x44:
			case 0x45:
			case 0x46: return new OffsetTarget(raf.readShort());
			case 0x47:
			case 0x48:
			case 0x49:
			case 0x4A:
			case 0x4B: return new TypeArgumentTarget(raf.readShort(), raf.readUnsignedByte());
			default:   throw  new TargetTypeException(targetType);
		}
	}
}
