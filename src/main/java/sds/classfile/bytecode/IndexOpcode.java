package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for opcode has index operand.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iload">
 * iload
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lload">
 * lload
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fload">
 * fload
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dload">
 * dload
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aload">
 * aload
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.istore">
 * istore
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lstore">
 * lstore
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fstore">
 * fstore
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.store">
 * dstore
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.astore">
 * astore
 * </a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ret">
 * ret
 * </a>.
 * @author inagakikenichi
 */
public class IndexOpcode extends OpcodeInfo {
	private int index;

	/**
	 * constructor.
	 * @param opcodeType opcode type
	 * @param pc index into the code array
	 */
	public IndexOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.index = raf.readByte();
	}

	/**
	 * returns index.
	 * @return index
	 */
	public int getIndex() {
		return index;
	}
}