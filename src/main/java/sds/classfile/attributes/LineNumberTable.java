package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class LineNumberTable extends AttributeInfo {
	/**
	 * array of line number table.
	 */
	LNTable[] lineNumberTable;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public LineNumberTable(int nameIndex, int length) {
		super(AttributeType.LineNumberTable, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public LNTable[] getLineNumberTable() {
		return lineNumberTable;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.lineNumberTable = new LNTable[raf.readShort()];
		for(int i = 0; i < lineNumberTable.length; i++) {
			lineNumberTable[i] = new LNTable(raf);
			if(i == lineNumberTable.length-1) {
				lineNumberTable[i].endPc = 0;
			} else if(i > 0) {
				lineNumberTable[i-1].endPc = lineNumberTable[i].startPc;
			}
		}
	}

	/**
	 * 
	 */
	public class LNTable {
		/**
		 * start number of opcode.
		 */
		int startPc;
		/**
		 * 
		 */
		int endPc;
		/**
		 * line number in source file.
		 */
		int lineNumber;

		/**
		 * 
		 * @param startPc
		 * @param lineNumber 
		 */
		LNTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.lineNumber = raf.readShort();
		}

		public int getStartPc() {
			return startPc;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public int getEndPc() {
			return endPc;
		}
	}
}
