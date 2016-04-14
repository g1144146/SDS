package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.8">
 * Constant_MethodHandle_Info</a>.
 * @author inagaki
 */
public class MethodHandleInfo extends ConstantInfo {
	/**
	 * kind of this method handle.
	 */
	int referenceKind;
	/**
	 * constant-pool entry index of constant info.
	 */
	int referenceIndex;
	
	 /**
	  * 
	  */
	public MethodHandleInfo() {
		super(ConstantType.C_METHOD_HANDLE);
	}

	/**
	 * returns kind of this method handle.
	 * @return kind of this method handle
	 */
	public int getReferenceKind() {
		return referenceKind;
	}

	/**
	 * returns constant-pool entry index of constant info.
	 * @return constant-pool entry index of constant info
	 */
	public int getReferenceIndex() {
		return referenceIndex;
	}

	/**
	 * returns desctiption of reference kind of this method handle.
	 * @return desctiption
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
		sb.append(super.toString()).append("\t").append(getRefKindValue())
			.append(":#").append(referenceIndex);
		return sb.toString();
	}
}