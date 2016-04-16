package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This adapter class is for
 * {@link LocalVariableTable <code>LocalVariableTable</code>}
 * and
 * {@link LocalVariableTypeTable <code>LocalVariableTypeTable</code>}.
 * @author inagaki
 */
public abstract class LocalVariable extends AttributeInfo {
	private LVTable[] localVariableTable;

	/**
	 * constructor.
	 * @param type attribute type
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	LocalVariable(AttributeType type, int nameIndex, int length) {
		super(type, nameIndex, length);
	}

	/**
	 * return local variable table.
	 * @return local variable table
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
	 * This class is for local variable table.
	 */
	public class LVTable {
		private int startPc;
		private int length;
		private int nameIndex;
		private int descriptorIndex;
		private int index;

		LVTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.length = raf.readShort();
			this.nameIndex = raf.readShort();
			this.descriptorIndex = raf.readShort();
			this.index = raf.readShort();
		}

		/**
		 * returns value.<br><br>
		 * if key is "start_pc", it returns
		 * start value of ranges in the code array at which
		 * local variable is active.<br>
		 * if key is "length", it returns
		 * range in the code array at which local variable
		 * is active.<br>
		 * if key is "name_index", it returns
		 * constant-pool entry index of local variable name.<br>
		 * if key is "descriptor", it returns
		 * constant-pool entry index of local variable's
		 * descriptor.<br>
		 * if key is "index", it returns
		 * index in the local variable array of
		 * the current frame.<br>
		 * by default, it returns -1.
		 * @param key value name
		 * @return value
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