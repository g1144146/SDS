package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagakikenichi
 */
abstract class LocalVariable extends AttributeInfo {

	/**
	 * 
	 */
	Table[] localVariableTable;

	/**
	 * 
	 * @param type
	 * @param nameIndex
	 * @param length 
	 */
	LocalVariable(AttributeType.Type type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public Table[] getTable() {
		return localVariableTable;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.localVariableTable = new Table[len];
		for(int i = 0; i < len; i++) {
			localVariableTable[i] = new Table(raf);
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
		Table(RandomAccessFile raf) throws IOException {
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
				case "start_pc":
					return startPc;
				case "length":
					return length;
				case "name_index":
					return nameIndex;
				case "descriptor":
					return descriptorIndex;
				case "index":
					return index;
				default:
					System.out.println("Invalid key: " + key);
					return -10000;
			}
		}
	}
}
