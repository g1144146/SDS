package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
public abstract class LocalVariable extends AttributeInfo {

	/**
	 * 
	 */
	LVTable[] localVariableTable;

	/**
	 * 
	 * @param type
	 * @param nameIndex
	 * @param length 
	 */
	LocalVariable(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public LVTable[] getTable() {
		return localVariableTable;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.localVariableTable = new LVTable[raf.readShort()];
		for(int i = 0; i < localVariableTable.length; i++) {
			localVariableTable[i] = new LVTable(raf);
		}
	}

	/**
	 * 
	 */
	public class LVTable {
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
		int nameIndex;
		/**
		 * 
		 */
		int descriptorIndex;
		/**
		 * 
		 */
		int index;

		/**
		 * 
		 * @param startPc
		 * @param length
		 * @param nameIndex
		 * @param descriptorIndex
		 * @param index 
		 */
		LVTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.length = raf.readShort();
			this.nameIndex = raf.readShort();
			this.descriptorIndex = raf.readShort();
			this.index = raf.readShort();
		}

		/**
		 * 
		 * @param key
		 * @return 
		 */
		public int getNumber(String key) {
			switch(key) {
				case "start_pc":   return startPc;
				case "length":     return length;
				case "name_index": return nameIndex;
				case "descriptor": return descriptorIndex;
				case "index":      return index;
				default:
					System.out.println("Invalid key: " + key);
					return -1;
			}
		}
	}
}