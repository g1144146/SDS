package sds.classfile.bytecode;

/**
 * This class is for exception of {@link OpcodeInfo <code>OpcodeInfo</code>}.
 * @author inagaki
 */
public class UndefinedOpcodeException extends Exception {
	/**
	 * constructor.
	 * @param opcode hex of opcode
	 */
	public UndefinedOpcodeException(int opcode) {
		super(Integer.toHexString(opcode) + " is undefined opcode.");
	}
}