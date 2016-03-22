package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.Attributes;
import sophomore.classfile.ConstantPool;
import sophomore.classfile.constantpool.Utf8Info;

/**
 *
 * @author inagaki
 */
public class Code extends AttributeInfo {
	/**
	 *
	 */
	int maxStack;
	/**
	 *
	 */
	int maxLocals;
	/**
	 *
	 */
	byte[] code;
	/**
	 *
	 */
	ExceptionTable[] exceptionTable;
	/**
	 *
	 */
	Attributes attr;

	/**
	 *
	 * @param nameIndex
	 * @param length
	 */
	public Code(int nameIndex, int length) {
		super(AttributeType.Type.Code, nameIndex, length);
	}

	/**
	 *
	 * @return
	 */
	public int getMaxStack() {
		return maxStack;
	}

	/**
	 *
	 * @return
	 */
	public int maxLocals() {
		return maxLocals;
	}

	/**
	 *
	 * @return
	 */
	public byte[] getCode() {
		return code;
	}

	/**
	 *
	 * @return
	 */
	public ExceptionTable[] getExceptionTable() {
		return exceptionTable;
	}

	/**
	 *
	 * @return
	 */
	public Attributes getAttr() {
		return attr;
	}

	public void read(RandomAccessFile raf, ConstantPool pool)
	throws IOException, UnknownAttributeTypeException {
		this.maxStack = raf.readShort(); // this.dataStream.readShort();
		this.maxLocals = raf.readShort();
		int codeLen = raf.readInt();
		this.code = new byte[codeLen];
		raf.readFully(code);
		int tableLen = raf.readShort();
		this.exceptionTable = new ExceptionTable[tableLen];
		for(int i = 0; i < tableLen; i++) {
			exceptionTable[i] = new ExceptionTable(raf);
		}
		int count = raf.readShort();
		this.attr = new Attributes(count);
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		for(int i = 0; i < count; i++) {
			int index = raf.readShort();
			int length = raf.readInt();
			String attrName = ((Utf8Info)pool.get(index-1)).getValue();
			AttributeInfo info = builder.build(attrName, index, length);
			info.read(raf);
			attr.add(i, info);
		}
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {}

	/**
	 *
	 */
	public class ExceptionTable {
		/**
		 *
		 */
		int startPc;
		/**
		 *
		 */
		int endPc;
		/**
		 *
		 */
		int handlerPc;
		/**
		 *
		 */
		int catchType;

		/**
		 *
		 * @param raf
		 * @throws IOException
		 */
		ExceptionTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.endPc = raf.readShort();
			this.handlerPc = raf.readShort();
			this.catchType = raf.readShort();
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
				case "end_pc":
					return endPc;
				case "handler_pc":
					return handlerPc;
				case "catch_type":
					return catchType;
				default:
					System.out.println("invalid key: " + key);
					return -10000;
			}
		}
	}
}
