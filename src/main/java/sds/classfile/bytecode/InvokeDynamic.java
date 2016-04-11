/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sds.classfile.bytecode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public class InvokeDynamic extends CpRefOpcode {

	public InvokeDynamic(int pc) {
		super(MnemonicTable.inovokedynamic, pc);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		super.read(raf);
		raf.skipBytes(2);
	}
}
