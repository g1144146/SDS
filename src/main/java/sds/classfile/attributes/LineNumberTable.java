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
	 * constrcutor.
	 */
	public LineNumberTable() {
		super(AttributeType.LineNumberTable);
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
			setEndPc(i);
		}
	}

	private void setEndPc(int index) {
		if(index == lineNumberTable.length-1) {
			lineNumberTable[index].endPc = lineNumberTable[index].getStartPc();
			setEndPc2(index);
			return;
		}
		if(index > 0) {
			lineNumberTable[index-1].endPc = lineNumberTable[index].startPc;
		}
	}

	private void setEndPc2(int index) {
		if(lineNumberTable.length > 1) {
			lineNumberTable[index-1].endPc = lineNumberTable[index].getStartPc();
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