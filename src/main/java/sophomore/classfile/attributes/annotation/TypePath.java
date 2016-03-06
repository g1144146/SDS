package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class TypePath {
	/**
	 * 
	 */
	int typePathKind;
	/**
	 * 
	 */
	int typeArgIndex;

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	TypePath(RandomAccessFile raf) throws IOException {
		this.typePathKind = raf.readByte();
		this.typeArgIndex = raf.readByte();
	}

	/**
	 * 
	 * @return 
	 */
	public int getPathKind() {
		return typePathKind;
	}

	/**
	 * 
	 * @return 
	 */
	public int getArgIndex() {
		return typeArgIndex;
	}
}
