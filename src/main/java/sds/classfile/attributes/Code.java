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
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3">Code Attribute</a>.
 * @author inagaki
 */
public class Code extends AttributeInfo {
	private int maxStack;
	private int maxLocals;
	private Opcodes opcodes;
	private ExceptionTable[] exceptionTable;
	private Attributes attr;

	/**
	 *
	 * @param nameIndex
	 * @param length
	 */
	public Code(int nameIndex, int length) {
		super(AttributeType.Code, nameIndex, length);
	}

	/**
	 * returns maximum depth of the operand stack of method.
	 * @return maximum depth.
	 */
	public int getMaxStack() {
		return maxStack;
	}

	/**
	 * returns number of local variables.
	 * @return number of local variables.
	 */
	public int maxLocals() {
		return maxLocals;
	}

	/**
	 * returns opcode sequence of method.
	 * @return opcode sequence
	 */
	public Opcodes getCode() {
		return opcodes;
	}

	/**
	 * returns exceptions of method.
	 * @return exceptions
	 */
	public ExceptionTable[] getExceptionTable() {
		return exceptionTable;
	}

	/**
	 * returns attributes of method.
	 * @return attributes
	 */
	public Attributes getAttr() {
		return attr;
	}

	/**
	 * reads Code Attribute from classfile.
	 * @param raf classfile stream
	 * @param pool constant-pool
	 * @throws Exception 
	 */
	public void read(RandomAccessFile raf, ConstantPool pool) throws Exception {
		this.maxStack = raf.readShort();
		this.maxLocals = raf.readShort();
		// extract opcode
		int codeLen = raf.readInt();
		int p = (int)raf.getFilePointer();
		this.opcodes = new Opcodes();
		try {
			int i;
			while((i = (int)raf.getFilePointer()) < codeLen + p) {
				int pc = (i - p);
				OpcodeInfo info = MnemonicTable.get(Byte.toUnsignedInt(raf.readByte()), pc);
				info.read(raf);
				opcodes.add(info.getPc(), info);
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
	 * This class is for exception table of method.
	 */
	public class ExceptionTable {
		private int startPc;
		private int endPc;
		private int handlerPc;
		private int catchType;

		ExceptionTable(RandomAccessFile raf) throws IOException {
			this.startPc = raf.readShort();
			this.endPc = raf.readShort();
			this.handlerPc = raf.readShort();
			this.catchType = raf.readShort();
		}

		/**
		 * returns value.<br><br>
		 * if key is "start_pc", it returns start value of
		 * ranges in the code array at which the exception
		 * handler is active.<br>
		 * if key is "end_pc", it returns end value of ranges
		 * in the code array at which the exception handler
		 * is active.<br>
		 * if key is "handler_pc", it returns value of start
		 * of exception handler.<br>
		 * if key is "catch_type", it returns constant-pool
		 * entry index exception class.<br>
		 * by default, it returns -1.
		 * @param key value name
		 * @return value
		 */
		public int getNumber(String key) {
			switch(key) {
				case "start_pc":   return startPc;
				case "end_pc":     return endPc;
				case "handler_pc": return handlerPc;
				case "catch_type": return catchType;
				default:
					System.out.println("invalid key: " + key);
					return -1;
			}
		}
	}
}