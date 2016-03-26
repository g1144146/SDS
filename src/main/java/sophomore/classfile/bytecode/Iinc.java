package sophomore.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class Iinc extends OpcodeInfo {

	int index;
	int _const;

	public Iinc(int pc) {
		super(MnemonicTable.iinc, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.index  = raf.readUnsignedByte();
		this._const = raf.readByte();
	}
}
