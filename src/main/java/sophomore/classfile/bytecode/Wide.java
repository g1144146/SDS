package sophomore.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Wide extends CpRefOpcode {
	/**
	 * 
	 */
	int constByte;

	public Wide(int pc) {
		super(MnemonicTable.wide, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int opcode = raf.readByte();
		if(opcode == MnemonicTable.iinc.getOpcode()) {
			super.read(raf);
		} else {
			this.constByte = raf.readShort();
		}
	}

	public int getConst() {
		return constByte;
	}
}
