package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto_w">
 * goto_w
 * </a>
 * and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.jsr_w">
 * jsr_w
 * </a>
 * @author inagaki
 */
public class BranchWideOpcode extends BranchOpcode {
	/**
	 * constrcutor.
	 * @param opcodeType opcode type
	 * @param pc index into the code array
	 */
	public BranchWideOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(ClassFileStream data) throws IOException {
		this.branch = data.readInt();
	}
}