package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

import static sds.util.DescriptorParser.parse;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.multianewarray">
 * multianewarray
 * </a>.
 * @author inagaki
 */
public class MultiANewArray extends CpRefOpcode {
	private int dimensions;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public MultiANewArray(int pc) {
		super(MnemonicTable.multianewarray, pc);
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		super.read(data, pool);
		this.dimensions = data.readByte();
		operand = parse(operand, false);	
	}

	/**
	 * return value of dimensions of the array.
	 * @return value of dimensions of the array
	 */
	public int getDimensions() {
		return dimensions;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MultiANewArray)) {
			return false;
		}
		MultiANewArray opcode = (MultiANewArray)obj;
		return super.equals(obj) && (dimensions == opcode.dimensions);
	}

	@Override
	public String toString() {
		return super.toString() + ", " + dimensions;
	}
}