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
		super(ConstantType.C_METHOD_HANDLE);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = System.getProperty("line.separator");
		sb.append(super.toString()).append(sep).append("\t")
			.append("reference kind : ").append(referenceKind).append(sep).append("\t")
			.append("reference index: ").append(referenceIndex);
		return sb.toString();
	}
}