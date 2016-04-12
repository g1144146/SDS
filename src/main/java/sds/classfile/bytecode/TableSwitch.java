package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class TableSwitch extends SwitchOpcode {

	int[] jumpOffsets;

	TableSwitch(int pc) {
		super(MnemonicTable.tableswitch, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		int low = raf.readInt();
		int high = raf.readInt();
		this.jumpOffsets = new int[high - low + 1];
		for(int i = 0; i < jumpOffsets.length; i++) {
			jumpOffsets[i] = raf.readInt();
		}
	}

	public int[] getJumpOffsets() {
		return jumpOffsets;
	}
}
