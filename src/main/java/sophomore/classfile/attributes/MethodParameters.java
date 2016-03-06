package sophomore.classfile.attributes;

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
		super(AttributeType.Type.MethodParameters, nameIndex, length);
	}

	public Parameters[] getParams() {
		return params;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readByte();
		this.params = new Parameters[len];
		for(int i = 0; i < len; i++) {
			params[i] = new Parameters(raf);
		}
	}

	/**
	 * 
	 */
	class Parameters {
		/**
		 * 
		 */
		int nameIndex;
		/**
		 * 
		 */
		int accessFlags;

		/**
		 * 
		 * @param raf
		 * @throws IOException 
		 */
		Parameters(RandomAccessFile raf) throws IOException {
			this.nameIndex = raf.readShort();
			this.accessFlags = raf.readShort();
		}
	}
}
