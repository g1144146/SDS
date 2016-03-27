package sophomore.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class LookupSwitch extends SwitchOpcode {
	LookupSwitch(int pc) {
		super(MnemonicTable.lookupswitch, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
	}
}
