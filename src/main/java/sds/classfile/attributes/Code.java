package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.Attributes;
import sds.classfile.ConstantPool;
import sds.classfile.bytecode.MnemonicTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.classfile.bytecode.UndefinedOpcodeException;
import sds.classfile.constantpool.Utf8Info;

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
		try {
			int i;
			while((i = (int)raf.getFilePointer()) < codeLen + p) {
				int pc = (i - p);
				OpcodeInfo info = MnemonicTable.get(Byte.toUnsignedInt(raf.readByte()), pc);
				info.read(raf);
				opcodes.add(info);
			}
		} catch(UndefinedOpcodeException e) {
			e.printStackTrace();
		}
		
		this.exceptionTable = new ExceptionTable[raf.readShort()];
		for(int i = 0; i < exceptionTable.length; i++) {
			exceptionTable[i] = new ExceptionTable(raf);
		}
		
		this.attr = new Attributes(raf.readShort());
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		for(int i = 0; i < attr.size(); i++) {
			int index = raf.readShort();
			String attrName = ((Utf8Info)pool.get(index-1)).getValue();
			AttributeInfo info = builder.build(attrName, index, raf.readInt());
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
