package sds.classfile.bytecode;

/**
 *
 * @author inagaki
 */
public class UndefinedOpcodeException extends Exception {
	public UndefinedOpcodeException(int opcode) {
		super(Integer.toHexString(opcode) + " is undefined opcode.");
	}
}
