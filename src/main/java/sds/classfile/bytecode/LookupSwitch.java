package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class LookupSwitch extends SwitchOpcode {
	int[] match;
	int[] offset;

	LookupSwitch(int pc) {
		super(MnemonicTable.lookupswitch, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		this.match  = new int[raf.readInt()];
		this.offset = new int[match.length];
		for(int i = 0; i < match.length; i++) {
			match[i] = raf.readInt();
			offset[i] = raf.readInt();
		}
	}

	public int[] getMatch() {
		return match;
	}

	public int[] getOffset() {
		return offset;
	}
}