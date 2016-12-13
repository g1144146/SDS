package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

import static sds.util.AccessFlags.get;

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
	 */
	public MethodParameters() {
		super(AttributeType.MethodParameters);
	}

	/**
	 * returns method parameters.
	 * @return method parameters
	 */
	public Parameters[] getParams() {
		return params;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.params = new Parameters[data.readByte()];
		for(int i = 0; i < params.length; i++) {
			params[i] = new Parameters(data, pool);
		}
	}

	/**
	 * This class is for method parameter.
	 */
	public class Parameters {
		private String name;
		private String accessFlag;

		Parameters(ClassFileStream data, ConstantPool pool) throws IOException {
			int nameIndex = data.readShort();
			int accessFlag = data.readShort();
			this.accessFlag = get(accessFlag, "local");
			this.name = extract(pool.get(nameIndex-1), pool);
		}

		/**
		 * returns method parameter.
		 * @return method parameter
		 */
		public String getName() {
			return name;
		}

		/**
		 * returns access flag of method parameter.
		 * @return access flag
		 */
		public String getAccessFlag() {
			return accessFlag;
		}
	}
}