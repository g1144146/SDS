package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lookupswitch">
 * lookupswitch
 * </a>.
 * @author inagaki
 */
public class LookupSwitch extends SwitchOpcode {
	private int[] match;
	private int[] offset;

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

	/**
	 * returns values of case keyword.
	 * @return values
	 */
	public int[] getMatch() {
		return match;
	}

	/**
	 * returns offsets.<br>
	 * jump point of each case keyword is "offset + pc".
	 * @return offsets
	 */
	public int[] getOffset() {
		return offset;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < match.length; i++) {
			sb.append(match[i]).append(", ")
				.append(offset[i]+getPc()).append("\n");
		}
		sb.append(getDefault()+getPc());
		return super.toString() + ": " + sb.toString();
	}
}