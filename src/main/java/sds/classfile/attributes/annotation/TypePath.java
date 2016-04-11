package sds.classfile.attributes.annotation;

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
		this.typeArgIndex = new int[raf.readUnsignedByte()];
		this.typePathKind = new int[typeArgIndex.length];
		for(int i = 0; i < typeArgIndex.length; i++) {
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
