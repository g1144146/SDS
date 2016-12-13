package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

import static sds.util.OperandExtractor.extractOperand;

/**
 * This class is for opcode has constant-pool entry index.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc">
 * ldc
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc_w">
 * ldc_w
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc2_w">
 * ldc2_w
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.getstatic">
 * getstatic
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.putstatic">
 * putstatic
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.getfield">
 * getfield
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.putfield">
 * putfield
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokevirtual">
 * invokevirtual
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokespecial">
 * invokespecial
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokestatic">
 * invokestatic
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.new">
 * new
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.checkcast">
 * checkcast
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.anewarray">
 * anewarray
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.instanceof">
 * instanceof
 * </a>
 * @author inagaki
 */
public class CpRefOpcode extends OpcodeInfo {
	int index;
	String operand;

	/**
	 * constrcutor.
	 * @param opcodeType opcode type
	 * @param pc index into the code array
	 */
	public CpRefOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		if(this.getOpcodeType() == MnemonicTable.ldc) {
			this.index = data.readUnsignedByte();
			this.operand = extractOperand(this, pool);
			return;
		}
		this.index = data.readShort();
		this.operand = extractOperand(this, pool);
	}

	/**
	 * returns constant-pool entry index.
	 * @return constant-pool entry index.
	 */
	public int getIndexByte() {
		return index;
	}

	/**
	 * returns operand which this opcode has.
	 * @return operand.
	 */
	public String getOperand() {
		return operand;
	}

	@Override
	public String toString() {
		return super.toString() + ": #" + index + "(" + operand + ")";
	}
}