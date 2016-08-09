package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokedynamic">
 * invokedynamic
 * </a>.
 * @author inagaki
 */
public class InvokeDynamic extends CpRefOpcode {
	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public InvokeDynamic(int pc) {
		super(MnemonicTable.inovokedynamic, pc);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		super.read(data, pool);
		data.skipBytes(2);
	}
}