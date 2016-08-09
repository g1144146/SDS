package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.Attributes;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.MnemonicTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.bytecode.Opcodes;
import sds.classfile.bytecode.UndefinedOpcodeException;
import sds.classfile.constantpool.Utf8Info;

import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3">
 * Code Attribute</a>.
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

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws Exception {
		this.maxStack = data.readShort();
		this.maxLocals = data.readShort();
		// extract opcode
		int codeLen = data.readInt();
		int p = (int)data.getFilePointer();
		this.opcodes = new Opcodes();
		
		try {
			int i;
			while((i = (int)data.getFilePointer()) < codeLen + p) {
				int pc = (i - p);
				OpcodeInfo info = MnemonicTable.get(Byte.toUnsignedInt(data.readByte()), pc);
				info.read(data, pool);
				opcodes.add(info.getPc(), info);
			}
		} catch(UndefinedOpcodeException e) {
			e.printStackTrace();
		}
		
		this.exceptionTable = new ExceptionTable[data.readShort()];
		for(int i = 0; i < exceptionTable.length; i++) {
			exceptionTable[i] = new ExceptionTable(data, pool);
		}
		
		this.attr = new Attributes(data.readShort());
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		for(int i = 0; i < attr.size(); i++) {
			int index = data.readShort();
			String attrName = ((Utf8Info)pool.get(index-1)).getValue();
			AttributeInfo info = builder.build(attrName, index, data.readInt());
			if(info.getType() == AttributeType.StackMapTable) {
				((StackMapTable)info).read(data, pool, opcodes);
			} else {
				info.read(data, pool);
			}
			attr.add(i, info);
		}
	}

	/**
	 * This class is for exception table of method.
	 */
	public class ExceptionTable {
		private int startPc;
		private int endPc;
		private int handlerPc;
		private String catchType;

		ExceptionTable(ClassFileStream data, ConstantPool pool) throws IOException {
			this.startPc = data.readShort();
			this.endPc = data.readShort();
			this.handlerPc = data.readShort();
			int catchType = data.readShort();
			if(catchType == 0) {
				this.catchType = "any";
			} else {
				this.catchType = extract(pool.get(catchType), pool);
			}
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
		 * by default, it returns -1.
		 * @param key value name
		 * @return value
		 */
		public int getNumber(String key) {
			switch(key) {
				case "start_pc":   return startPc;
				case "end_pc":     return endPc;
				case "handler_pc": return handlerPc;
				default:
					System.out.println("invalid key: " + key);
					return -1;
			}
		}

		/**
		 * returns exception class.
		 * @return exception class
		 */
		public String getCatchType() {
			return catchType;
		}
	}
}