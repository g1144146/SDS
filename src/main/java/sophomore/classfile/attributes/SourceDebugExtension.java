/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class SourceDebugExtension extends AttributeInfo {
	/**
	 * 
	 */
	byte[] debugExtension;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public SourceDebugExtension(int nameIndex, int length) {
		super(AttributeType.SourceDebugExtension, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public byte[] getDebugExtension() {
		return debugExtension;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.debugExtension = new byte[this.attrLen];
		raf.readFully(debugExtension);
	}
}
