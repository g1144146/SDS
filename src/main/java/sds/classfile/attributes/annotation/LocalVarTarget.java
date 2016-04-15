package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for localvar_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class LocalVarTarget extends AbstractTargetInfo {
	private LVTTable[] table;

	LocalVarTarget(RandomAccessFile raf) throws IOException {
		super(TargetInfoType.LocalVarTarget);
		this.table = new LVTTable[raf.readShort()];
		for(int i = 0; i < table.length; i++) {
			table[i] = new LVTTable(raf);
		}
	}

	/**
	 * returns local var table.
	 * @return local var table
	 */
	public LVTTable[] getTable() {
		return table;
	}

	/**
	 * This class is for table which localval_target has.
	 */
	public class LVTTable {
		private int startPc;
		private int length;
		private int index;

		LVTTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.length  = raf.readShort();
			this.index   = raf.readShort();
		}

		/**
		 * returns start value of ranges.
		 * the start value of ranges in the code array at which
		 * local variable is active.
		 * @return start value
		 */
		public int getStartPc() {
			return startPc;
		}

		/**
		 * returns range in the code array at which local variable is active.
		 * @return range in the code array
		 */
		public int getLen() {
			return length;
		}

		/**
		 * returns index in the local variable array of the current frame.
		 * @return index in the local variable array
		 */
		public int getIndex() {
			return index;
		}
	}
}
