package sophomore.classfile.attributes;

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
		int len = raf.readShort();
		this.lineNumberTable = new LNTable[len];
		for(int i = 0; i < len; i++) {
			lineNumberTable[i] = new LNTable(raf);
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
	}
}
