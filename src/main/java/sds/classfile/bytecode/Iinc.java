package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iinc">
 * iinc
 * </a>.
 * @author inagaki
 */
public class Iinc extends OpcodeInfo {
	private int index;
	private int _const;

	/**
	 * constructor.
	 * @param pc index into the code array
	 */
	public Iinc(int pc) {
		super(MnemonicTable.iinc, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.index  = raf.readUnsignedByte();
		this._const = raf.readByte();
	}

	/**
	 * returns index into the local variable array of the current frame.
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * returns const.
	 * @return const
	 */
	public int getConst() {
		return _const;
	}
}