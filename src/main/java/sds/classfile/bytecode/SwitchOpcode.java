package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This adapter class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lookupswitch">
 * lookupswitch
 * </a> and 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.tableswitch">
 * tableswitch
 * </a>.
 * @author inagaki
 */
abstract class SwitchOpcode extends OpcodeInfo {
	int defaultByte;

	public SwitchOpcode(MnemonicTable opcodeType, int pc) {
		super(opcodeType, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		for(int i = 1; ((i+this.getPc()) % 4) != 0; i++) {
			raf.readByte();
		}
//		raf.skipBytes((this.pc % 4));
		this.defaultByte = raf.readInt();
	}

	/**
	 * returns default byte.<br>
	 * jump point of default key is "defaultByte + pc".
	 * @return default byte
	 */
	public int getDefault() {
		return defaultByte;
	}
}