package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class ChopFrame extends SameFrame {
	/**
	 * 
	 */
	int offsetDelta;

	ChopFrame(RandomAccessFile raf) throws IOException {
		super(raf.readByte());
		this.offsetDelta = raf.readShort();
	}

	public int getOffset() {
		return offsetDelta;
	}
}
