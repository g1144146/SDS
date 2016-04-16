package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

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
	public void read(RandomAccessFile raf) throws IOException {
		int type = raf.readUnsignedByte();
		switch(type) {
			case 4:  this.atype = "T_BOOLEAN"; break;
			case 5:  this.atype = "T_CHAR";    break;
			case 6:  this.atype = "T_FLOAT";   break;
			case 7:  this.atype = "T_DOUBLE";  break;
			case 8:  this.atype = "T_BYTE";    break;
			case 9:  this.atype = "T_SHORT";   break;
			case 10: this.atype = "T_INT";     break;
			case 11: this.atype = "T_LONG";    break;
			default:
				System.out.println(">>> unknown type: " + type);
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
}