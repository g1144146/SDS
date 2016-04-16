package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

 /**
 * This class is for opcode has branch.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond">
 * ifeq, ifne, iflt, ifge, ifgt, ifle
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond">
 * if_icmpeq, if_icmpne, if_icmplt, if_icmpge, if_icmpgt, if_icmple
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_acmp_cond">
 * if_acmpeq, if_acmpne
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto">
 * goto
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.jsr">
 * jsr
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifnull">
 * ifnull
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifnonnull">
 * ifnonnull
 * </a>.
 * @author inagaki
 */
public class BranchOpcode extends OpcodeInfo {
	int branch;

	/**
	 * constructor.
	 * @param opcodeType opcode type
	 * @param pc index into the code array
	 */
	public BranchOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.branch = raf.readUnsignedShort();
	}

	/**
	 * returns branch bytes.<br>
	 * value of jump point.
	 * @return branch bytes
	 */
	public int getBranch() {
		return branch;
	}
}