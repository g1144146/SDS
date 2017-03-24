package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.newarray">
 * newarray
 * </a>.
 * @author inagaki
 */
public class NewArray extends OpcodeInfo {
	private String atype;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public NewArray(int pc) {
		super(MnemonicTable.newarray, pc);
	}

	@Override
	public void read(ClassFileStream data) throws IOException {
		int type = data.readUnsignedByte();
		switch(type) {
			case 4:  this.atype = "boolean"; break;
			case 5:  this.atype = "char";    break;
			case 6:  this.atype = "float";   break;
			case 7:  this.atype = "double";  break;
			case 8:  this.atype = "byte";    break;
			case 9:  this.atype = "short";   break;
			case 10: this.atype = "int";     break;
			case 11: this.atype = "long";    break;
			default:
				System.out.println("[unknown type]: " + type);
				break;
		}
	}

	/**
	 * returns array type.
	 * @return array type
	 */
	public String getType() {
		return atype;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NewArray)) {
			return false;
		}
		NewArray opcode = (NewArray)obj;
		return super.equals(obj) && atype.equals(opcode.atype);
	}

	@Override
	public String toString() {
		return super.toString() + ": " + atype;
	}
}