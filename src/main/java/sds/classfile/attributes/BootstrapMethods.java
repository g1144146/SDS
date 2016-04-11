package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class BootstrapMethods extends AttributeInfo {
	/**
	 * 
	 */
	BSM[] bsm;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public BootstrapMethods(int nameIndex, int length) {
		super(AttributeType.BootstrapMethods, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.bsm = new BSM[raf.readShort()];
		for(int i = 0; i < bsm.length; i++) {
			bsm[i] = new BSM(raf);
		}
	}

	public BSM[] getBSM() {
		return bsm;
	}

	/**
	 * 
	 */
	public class BSM {
		/**
		 * The constant_pool entry at that index must be a CONSTANT_MethodHandle_info structure.
		 */
		int bsmRef;
		/**
		 * The constant_pool entry at that index must be a CONSTANT_String_info
		 * , CONSTANT_Class_info, CONSTANT_Integer_info, CONSTANT_Long_info, CONSTANT_Float_info
		 * , CONSTANT_Double_info, CONSTANT_MethodHandle_info, or CONSTANT_MethodType_info structure.
		 */
		int[] bootstrapArgs;

		/**
		 * 
		 * @param raf
		 * @throws IOException 
		 */
		BSM(RandomAccessFile raf) throws IOException {
			this.bsmRef = raf.readShort();
			this.bootstrapArgs = new int[raf.readShort()];
			for(int i = 0; i < bootstrapArgs.length; i++) {
				bootstrapArgs[i] = raf.readShort();
			}
		}

		public int getBSMRef() {
			return bsmRef;
		}

		public int[] getBSMArgs() {
			return bootstrapArgs;
		}
	}
}
