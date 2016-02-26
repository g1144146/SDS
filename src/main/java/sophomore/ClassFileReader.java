package sophomore;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.ConstantPool;
import sophomore.classfile.Fields;
import sophomore.classfile.FieldInfo;
import sophomore.classfile.Methods;
import sophomore.classfile.MethodInfo;
/**
 * 
 * @author inagaki
 */
public class ClassFileReader {
	/**
	 * 
	 */
	private String fileName;
	/**
	 * 
	 */
	private ClassFile cf;

	/**
	 * 
	 * @param file 
	 */
	public ClassFileReader(String file) {
		this.fileName = file;
		cf = new ClassFile();
	}

	/**
	 * 
	 */
	public void read() {
		try(RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
			readHeaders(raf);
			readConstantPool(raf, raf.readShort());
			readAccessFlag(raf);
			readClass(raf);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	private void readHeaders(RandomAccessFile raf) throws IOException {
		cf.magicNumber = raf.readInt(); // 4byte
		cf.minorVersion = raf.readShort(); // 2byte
		cf.majorVersion = raf.readShort(); // 2byte
	}

	/**
	 * 
	 * @param raf
	 * @param constantPoolCount
	 * @throws IOException 
	 */
	private void readConstantPool(RandomAccessFile raf, int constantPoolCount) throws IOException {
		ConstantPool cp = new ConstantPool(constantPoolCount);
		for(int i = 0; i < constantPoolCount; i++) {
			
		}
	}

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	private void readAccessFlag(RandomAccessFile raf) throws IOException {
		cf.accessFlag = raf.readShort();
	}

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	private void readClass(RandomAccessFile raf) throws IOException {
		cf.thisClass = raf.readShort();
		cf.superClass = raf.readShort();
	}

	/**
	 * 
	 * @param raf
	 * @param interfaceCount
	 * @throws IOException 
	 */
	private void readInterfaces(RandomAccessFile raf, int interfaceCount) throws IOException {
		cf.interfaceCount = interfaceCount;
		cf.interfaces = new int[interfaceCount];
		for(int i = 0; i < interfaceCount; i++) {
			cf.interfaces[i] = raf.readShort();
		}
	}

	/**
	 * 
	 * @param raf
	 * @param fieldCount
	 * @throws IOException 
	 */
	private void readFields(RandomAccessFile raf, int fieldCount) throws IOException {
		cf.fields = new Fields(fieldCount);
		for(int i = 0; i < fieldCount; i++) {
			FieldInfo info = new FieldInfo();
			cf.fields.addElement(info);
		}
	}

	/**
	 * 
	 * @param raf
	 * @param methodCount
	 * @throws IOException 
	 */
	private void readMethods(RandomAccessFile raf, int methodCount) throws IOException {
		cf.methods = new Methods(methodCount);
		for(int i = 0; i < methodCount; i++) {
			MethodInfo info = new MethodInfo();
			cf.methods.addElement(info);
		}
	}

	/**
	 * 
	 * @param raf
	 * @param attrCount
	 * @throws IOException 
	 */
	private void readAttributes(RandomAccessFile raf, int attrCount) throws IOException {
		
	}

	/**
	 * 
	 * @return 
	 */
	public ClassFile getClassFile() {
		return cf;
	}
}