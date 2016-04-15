package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">BootstrapMethods Attribute</a>.
 * @author inagaki
 */
public class BootstrapMethods extends AttributeInfo {
	BSM[] bsm;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name.
	 * @param length attribute length
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

	/**
	 * returns bootstrap methods table.
	 * @return bootstrap methods table
	 */
	public BSM[] getBSM() {
		return bsm;
	}

	/**
	 * This class is for entry in the bootstrap methods table.
	 */
	public class BSM {
		int bsmRef;
		int[] bootstrapArgs;

		BSM(RandomAccessFile raf) throws IOException {
			this.bsmRef = raf.readShort();
			this.bootstrapArgs = new int[raf.readShort()];
			for(int i = 0; i < bootstrapArgs.length; i++) {
				bootstrapArgs[i] = raf.readShort();
			}
		}

		/**
		 * reference index of bootstrap method.
		 * @return reference index
		 */
		public int getBSMRef() {
			return bsmRef;
		}

		/**
		 * index of bootstrap method arguments.
		 * @return index of bootstrap method arguments
		 */
		public int[] getBSMArgs() {
			return bootstrapArgs;
		}
	}
}