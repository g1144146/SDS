package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.12">
 * LineNumberTable Attribute</a>.
 * @author inagaki
 */
public class LineNumberTable extends AttributeInfo {
	private LNTable[] lineNumberTable;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public LineNumberTable(int nameIndex, int length) {
		super(AttributeType.LineNumberTable, nameIndex, length);
	}

	/**
	 * returns line number table.
	 * @return line number table
	 */
	public LNTable[] getLineNumberTable() {
		return lineNumberTable;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.lineNumberTable = new LNTable[data.readShort()];
		for(int i = 0; i < lineNumberTable.length; i++) {
			lineNumberTable[i] = new LNTable(data);
			if(i == lineNumberTable.length-1) {
				lineNumberTable[i].endPc = lineNumberTable[i].getStartPc();
				if(lineNumberTable.length > 1) {
					lineNumberTable[i-1].endPc = lineNumberTable[i].getStartPc();
				}
			} else if(i > 0) {
				lineNumberTable[i-1].endPc = lineNumberTable[i].startPc;
			}
		}
	}

	/**
	 * This class is for line number table.
	 */
	public class LNTable {
		private int startPc;
		private int endPc;
		private int lineNumber;

		LNTable(ClassFileStream data) throws IOException {
			this.startPc = data.readShort();
			this.lineNumber = data.readShort();
		}

		/**
		 * returns start number of opcode.
		 * @return start number
		 */
		public int getStartPc() {
			return startPc;
		}

		/**
		 * returns line number in source file.
		 * @return line number
		 */
		public int getLineNumber() {
			return lineNumber;
		}

		/**
		 * returns end number of opcode.
		 * @return end number
		 */
		public int getEndPc() {
			return endPc;
		}
	}
}