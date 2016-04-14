package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This adapter class is for LocalVariable Attribute.
 * @author inagaki
 */
public abstract class LocalVariable extends AttributeInfo {

	/**
	 * local variable table.
	 */
	LVTable[] localVariableTable;

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
		/**
		 * start value of ranges in the code array at which local variable is active.
		 */
		int startPc;
		/**
		 * range in the code array at which local variable is active.
		 */
		int length;
		/**
		 * constant-pool entry index of local variable name.
		 */
		int nameIndex;
		/**
		 * constant-pool entry index of local variable's descriptor.
		 */
		int descriptorIndex;
		/**
		 *  index in the local variable array of the current frame.
		 */
		int index;

		/**
		 *  constructor.
		 * @param startPc start value of ranges in the code array at which local variable is active
		 * @param length range in the code array at which local variable is active
		 * @param nameIndex constant-pool entry index of local variable name
		 * @param descriptorIndex constant-pool entry index of local variable's descriptor
		 * @param index index in the local variable array of the current frame
		 */
		LVTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.length = raf.readShort();
			this.nameIndex = raf.readShort();
			this.descriptorIndex = raf.readShort();
			this.index = raf.readShort();
		}

		/**
		 * returns value.
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
