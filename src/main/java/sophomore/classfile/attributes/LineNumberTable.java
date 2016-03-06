package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class LineNumberTable extends AttributeInfo {
	/**
	 * 
	 */
	Table[] lineNumberTable;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public LineNumberTable(int nameIndex, int length) {
		super(AttributeType.Type.LineNumberTable, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public Table[] getLineNumberTable() {
		return lineNumberTable;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.lineNumberTable = new Table[len];
		for(int i = 0; i < len; i++) {
			lineNumberTable[i] = new Table(raf);
		}
	}

	/**
	 * 
	 */
	class Table {
		/**
		 * 
		 */
		int startPc;
		/**
		 * 
		 */
		int lineNumber;

		/**
		 * 
		 * @param startPc
		 * @param lineNumber 
		 */
		Table(RandomAccessFile raf) throws IOException {
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
