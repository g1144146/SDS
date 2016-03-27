package sophomore.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class TableSwitch extends SwitchOpcode {
	TableSwitch(int pc) {
		super(MnemonicTable.tableswitch, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
	}
}
