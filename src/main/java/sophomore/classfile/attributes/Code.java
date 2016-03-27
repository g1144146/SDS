package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.Attributes;
import sophomore.classfile.ConstantPool;
import sophomore.classfile.bytecode.MnemonicTable;
import sophomore.classfile.bytecode.OpcodeInfo;
import sophomore.classfile.bytecode.Opcodes;
import sophomore.classfile.bytecode.UndefinedOpcodeException;
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
	Opcodes opcodes;
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
		super(AttributeType.Code, nameIndex, length);
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
	public Opcodes getCode() {
		return opcodes;
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
	throws IOException, AttributeTypeException {
		this.maxStack = raf.readShort();
		this.maxLocals = raf.readShort();
		int codeLen = raf.readInt();
		int p = (int)raf.getFilePointer();
		this.opcodes = new Opcodes();
//		System.out.println(codeLen + ", " + p);
		try {
			int i;
			while((i = (int)raf.getFilePointer()) < codeLen + p) {
				int pc = (i - p);
				int op = Byte.toUnsignedInt(raf.readByte());
				OpcodeInfo info = MnemonicTable.get(op, pc);
				info.read(raf);
				opcodes.add(info);
			}
		} catch(UndefinedOpcodeException e) {
			e.printStackTrace();
		}
//		raf.readFully(code);
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
			System.out.println(index + ", " + length + ", "+info.getType());
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
				case "start_pc":   return startPc;
				case "end_pc":     return endPc;
				case "handler_pc": return handlerPc;
				case "catch_type": return catchType;
				default:
					System.out.println("invalid key: " + key);
					return -10000;
			}
		}
	}
}
