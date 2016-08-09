package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">
 * BootstrapMethods Attribute</a>.
 * @author inagaki
 */
public class BootstrapMethods extends AttributeInfo {
	private BSM[] bsm;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name.
	 * @param length attribute length
	 */
	public BootstrapMethods(int nameIndex, int length) {
		super(AttributeType.BootstrapMethods, nameIndex, length);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.bsm = new BSM[data.readShort()];
		for(int i = 0; i < bsm.length; i++) {
			bsm[i] = new BSM(data, pool);
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
		private String bsmRef;
		private String[] bootstrapArgs;

		BSM(ClassFileStream data, ConstantPool pool) throws IOException {
			this.bsmRef = extract(pool.get(data.readShort()-1), pool);
			this.bootstrapArgs = new String[data.readShort()];
			for(int i = 0; i < bootstrapArgs.length; i++) {
				bootstrapArgs[i] = extract(pool.get(data.readShort()-1), pool);
			}
		}

		/**
		 * returns bootstrap method.
		 * @return bootstrap method
		 */
		public String getBSMRef() {
			return bsmRef;
		}

		/**
		 * retunrs bootstrap method arguments.
		 * @return bootstrap method arguments
		 */
		public String[] getBSMArgs() {
			return bootstrapArgs;
		}
	}
}