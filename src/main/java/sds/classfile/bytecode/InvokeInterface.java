package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokeinterface">
 * invokeinterface
 * </a>.
 * @author inagaki
 */
public class InvokeInterface extends CpRefOpcode {
	private int count;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public InvokeInterface(int pc) {
		super(MnemonicTable.invokeinterface, pc);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		super.read(data, pool);
		this.count = data.readUnsignedByte();
		data.skipBytes(1);
	}

	/**
	 * returns count.
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof InvokeInterface)) {
			return false;
		}
		InvokeInterface opcode = (InvokeInterface)obj;
		return super.equals(obj) && (count == opcode.count);
	}

	@Override
	public String toString() {
		return super.toString() + ", " + count;
	}
}