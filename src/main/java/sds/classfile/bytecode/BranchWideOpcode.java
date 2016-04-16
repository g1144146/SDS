package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for opcode has branch (wide index).<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto_w">
 * goto_w
 * </a>, 
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
	public void read(RandomAccessFile raf) throws IOException {
		this.branch = raf.readInt();
	}
}