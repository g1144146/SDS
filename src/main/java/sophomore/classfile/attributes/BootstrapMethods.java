package sophomore.classfile.attributes;

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
		super(AttributeType.Type.BootstrapMethods, nameIndex, length);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.bsm = new BSM[len];
		for(int i = 0; i < len; i++) {
			bsm[i] = new BSM(raf);
		}
	}

	/**
	 * 
	 */
	class BSM {
		/**
		 * 
		 */
		int bsmRef;
		/**
		 * 
		 */
		int[] bootstrapArgs;

		/**
		 * 
		 * @param raf
		 * @throws IOException 
		 */
		BSM(RandomAccessFile raf) throws IOException {
			this.bsmRef = raf.readShort();
			int len = raf.readShort();
			this.bootstrapArgs = new int[len];
			for(int i = 0; i < len; i++) {
				bootstrapArgs[i] = raf.readShort();
			}
		}
	}
}
