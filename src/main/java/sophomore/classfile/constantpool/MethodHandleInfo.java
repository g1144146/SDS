package sophomore.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author inagaki
 */
public class MethodHandleInfo extends ConstantInfo {
	/**
	 * 
	 */
	int referenceKind;
	/**
	 * 
	 */
	int referenceIndex;

	 /**
	  * 
	  */
	public MethodHandleInfo() {
		super(ConstantType.C_MethodHandle);
	}

	/**
	 * 
	 * @return 
	 */
	public int getReferenceKind() {
		return referenceKind;
	}

	/**
	 * 
	 * @return 
	 */
	public int getReferenceIndex() {
		return referenceIndex;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.referenceKind  = raf.readByte();
		this.referenceIndex = raf.readShort();
	}
}