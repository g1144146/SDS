package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
  * This class is for
  * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.24">
  * MethodParameters Attribute</a>.
 * @author inagaki
 */
public class MethodParameters extends AttributeInfo {
	private Parameters[] params;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public MethodParameters(int nameIndex, int length) {
		super(AttributeType.MethodParameters, nameIndex, length);
	}

	/**
	 * returns method parameters.
	 * @return method parameters
	 */
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
	 * This class is for method parameter.
	 */
	public class Parameters {
		int nameIndex;
		int accessFlag;

		Parameters(RandomAccessFile raf) throws IOException {
			this.nameIndex = raf.readShort();
			this.accessFlag = raf.readShort();
		}

		/**
		 * returns constant-pool entry index of method parameter.
		 * @return constant-pool entry index of method parameter
		 */
		public int getNameIndex() {
			return nameIndex;
		}

		/**
		 * returns access flag of method parameter.
		 * @return access flag
		 */
		public int getAccessFlag() {
			return accessFlag;
		}
	}
}