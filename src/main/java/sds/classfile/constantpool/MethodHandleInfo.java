package sds.classfile.constantpool;

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

	/**
	 * 
	 * @return 
	 */
	public String getRefKindValue() {
		switch(referenceKind) {
			case 1:  return "REF_getField";
			case 2:  return "REF_getStatic";
			case 3:  return "REF_putField";
			case 4:  return "REF_putStatic";
			case 5:  return "REF_invokeVirtual";
			case 6:  return "REF_invokeStatic";
			case 7:  return "REF_invokeSpecial";
			case 8:  return "REF_newInvokeSpecial";
			case 9:  return "REF_invokeInterface";
			default: return ">>> unknown reference kind <<<";
		}
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.referenceKind  = raf.readByte();
		this.referenceIndex = raf.readShort();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t#").append(referenceKind)
			.append(":#").append(referenceIndex);
		return sb.toString();
	}
}