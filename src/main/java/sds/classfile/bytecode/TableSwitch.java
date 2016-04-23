package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.tableswitch">
 * tableswitch
 * </a>.
 * @author inagaki
 */
public class TableSwitch extends SwitchOpcode {
	private int[] jumpOffsets;

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

	/**
	 * returns jump offsets.<br>
	 * jump point of each case keyword is "jump offset + pc".
	 * @return offset
	 */
	public int[] getJumpOffsets() {
		return jumpOffsets;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < jumpOffsets.length; i++) {
			sb.append(jumpOffsets[i]+getPc()).append("\n");
		}
		sb.append(getDefault()+getPc());
		return super.toString() + ": " + sb.toString();
	}
}