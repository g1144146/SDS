package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class TypePath {
	/**
	 * 
	 */
	int[] typePathKind;
	/**
	 * 
	 */
	int[] typeArgIndex;

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	TypePath(RandomAccessFile raf) throws IOException {
		int len = raf.readUnsignedByte();
		this.typeArgIndex = new int[len];
		this.typePathKind = new int[len];
		for(int i = 0; i < len; i++) {
			this.typePathKind[i] = raf.readByte();
			this.typeArgIndex[i] = raf.readByte();
		}
	}

	/**
	 * 
	 * @return 
	 */
	public int[] getPathKind() {
		return typePathKind;
	}

	/**
	 * 
	 * @return 
	 */
	public int[] getArgIndex() {
		return typeArgIndex;
	}
}
