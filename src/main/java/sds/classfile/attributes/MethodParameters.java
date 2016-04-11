package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class MethodParameters extends AttributeInfo {
	/**
	 * 
	 */
	Parameters[] params;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public MethodParameters(int nameIndex, int length) {
		super(AttributeType.MethodParameters, nameIndex, length);
	}

	public Parameters[] getParams() {
		return params;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.params = new Parameters[raf.readByte()];
		for(int i = 0; i < params.length; i++) {
			params[i] = new Parameters(raf);
		}
	}

	/**
	 * 
	 */
	public class Parameters {
		/**
		 * 
		 */
		int nameIndex;
		/**
		 * 
		 */
		int accessFlag;

		/**
		 * 
		 * @param raf
		 * @throws IOException 
		 */
		Parameters(RandomAccessFile raf) throws IOException {
			this.nameIndex = raf.readShort();
			this.accessFlag = raf.readShort();
		}

		public int getNameIndex() {
			return nameIndex;
		}

		public int getAccessFlag() {
			return accessFlag;
		}
	}
}
