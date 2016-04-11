package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class NewArray extends OpcodeInfo {

	String atype;

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

	public String getType() {
		return atype;
	}
}
