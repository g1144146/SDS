package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

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
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.localVariableTable = new LVTable[data.readShort()];
		for(int i = 0; i < localVariableTable.length; i++) {
			localVariableTable[i] = new LVTable(data, pool);
		}
	}

	/**
	 * This class is for local variable table.
	 */
	public class LVTable {
		private int startPc;
		private int length;
		private int index;
		private String name;
		private String desc;

		LVTable(ClassFileStream data, ConstantPool pool) throws IOException {
			this.startPc = data.readShort();
			this.length = data.readShort();
			int nameIndex = data.readShort();
			int descIndex = data.readShort();
			this.index = data.readShort();
			
			this.name = extract(pool.get(nameIndex-1), pool);
			this.desc = (descIndex-1 >= 0) ? parse(extract(pool.get(descIndex-1), pool)) : "";
		}

		/**
		 * returns value.<br><br>
		 * if key is "start_pc", it returns
		 * start value of ranges in the code array at which
		 * local variable is active.<br>
		 * if key is "length", it returns
		 * range in the code array at which local variable
		 * is active.<br>
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
				case "index":      return index;
				default:
					System.out.println("Invalid key: " + key);
					return -1;
			}
		}

		/**
		 * returns local variable name.
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * returns local variable descriptor.
		 * @return descriptor
		 */
		public String getDesc() {
			return desc;
		}
	}
}