package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.wide">
 * wide
 * </a>.
 * @author inagaki
 */
public class Wide extends CpRefOpcode {
	private int constByte = -1;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public Wide(int pc) {
		super(MnemonicTable.wide, pc);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		if(data.readByte() == MnemonicTable.iinc.getOpcode()) {
			super.read(data, pool);
			return;
		}
		this.constByte = data.readShort();
	}

	/**
	 * returns const.<br>
	 * if opcode item of this opcode is {@link Wide <code>Wide</code>}, const value equals -1.
	 * @return const
	 */
	public int getConst() {
		return constByte;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Wide)) {
			return false;
		}
		Wide opcode = (Wide)obj;
		return super.equals(obj) && (constByte == opcode.constByte);
	}

	@Override
	public String toString() {
		return super.toString() + ", " + constByte;
	}
}