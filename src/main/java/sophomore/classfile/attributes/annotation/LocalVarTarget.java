package sophomore.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class LocalVarTarget extends AbstractTargetInfo {
	/**
	 * 
	 */
	LVTTable[] table;

	LocalVarTarget(RandomAccessFile raf) throws IOException {
		super(TargetInfoType.LocalVarTarget);
		int len = raf.readShort();
		this.table = new LVTTable[len];
		for(int i = 0; i < len; i++) {
			table[i] = new LVTTable(raf);
		}
	}

	/**
	 * 
	 * @return 
	 */
	public LVTTable[] getTable() {
		return table;
	}
	
	public class LVTTable {
		/**
		 * 
		 */
		int startPc;
		/**
		 * 
		 */
		int length;
		/**
		 * 
		 */
		int index;

		LVTTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.length  = raf.readShort();
			this.index   = raf.readShort();
		}

		public int getStartPc() {
			return startPc;
		}

		public int getLen() {
			return length;
		}

		public int getIndex() {
			return index;
		}
	}
}
