package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class TargetInfo {
	// item of type_parameter_target
	/**
	 * 
	 */
	int typeParameterIndex;

	// item of supertype_target
	/**
	 * 
	 */
	int superTypeIndex;
	
	// items of type_parameter_bound_target
	/**
	 * 
	 */
	int typeParameterBoundTypeIndex;
	/**
	 * 
	 */
	int boundIndex;

	/**
	 * has no value.
	 */
	int emptyTarget = -1;
	/**
	 * 
	 */
	int formalParameterIndex;
	/**
	 * 
	 */
	int throwsTypeIndex;
	/**
	 * 
	 */
	LocalVarTarget[] localVarTarget;
	/**
	 * 
	 */
	int catchTargetExceptionTableIndex;
	/**
	 * 
	 */
	int offsetTarget;
	/**
	 * 
	 */
	int typeArgumentOffset;
	/**
	 * 
	 */
	int typeArgumentTypeIndex;

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	TargetInfo(RandomAccessFile raf) throws IOException {
		this.typeParameterIndex = raf.readByte();
		this.superTypeIndex = raf.readShort();
		this.typeParameterBoundTypeIndex = raf.readByte();
		this.boundIndex = raf.readByte();
		// ignore: emptyTarget 
		this.formalParameterIndex = raf.readByte();
		this.throwsTypeIndex = raf.readShort();
		int len = raf.readShort();
		this.localVarTarget = new LocalVarTarget[len];
		for(int i = 0; i < len; i++) {
			localVarTarget[i] = new LocalVarTarget(raf);
		}
		this.catchTargetExceptionTableIndex = raf.readShort();
		this.offsetTarget = raf.readShort();
		this.typeArgumentOffset = raf.readShort();
		this.typeArgumentTypeIndex = raf.readByte();
	}

	/**
	 * 
	 * @return 
	 */
	public LocalVarTarget[] getLocalVarTarget() {
		return localVarTarget;
	}

	/**
	 * 
	 * @param key
	 * @return 
	 */
	public int getNumber(String key) {
		switch(key) {
			case "type_param":
				return typeParameterIndex;
			case "super_type":
				return superTypeIndex;
			case "bound_type":
				return typeParameterBoundTypeIndex;
			case "bound":
				return boundIndex;
			case "formal":
				return formalParameterIndex;
			case "throws":
				return throwsTypeIndex;
			case "catch":
				return catchTargetExceptionTableIndex;
			case "offset":
				return offsetTarget;
			case "arg_offset":
				return typeArgumentOffset;
			case "arg_type":
				return typeArgumentTypeIndex;
			default:
				System.out.println("unknown key: " + key);
				return -10000;
		}
	}

	/**
	 * 
	 */
	class LocalVarTarget {
		/**
		 * 
		 */
		int startPc;
		/**
		 * 
		 */
		int length;
		/**
		 * 
		 */
		int index;

		/**
		 * 
		 * @param raf
		 * @throws IOException 
		 */
		LocalVarTarget(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.length  = raf.readShort();
			this.index   = raf.readShort();
		}

		public int getStartPc() {
			return startPc;
		}

		public int getLength() {
			return length;
		}

		public int getIndex() {
			return index;
		}
	}
}