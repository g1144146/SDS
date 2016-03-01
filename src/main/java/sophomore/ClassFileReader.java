package sophomore;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.ConstantPool;
import sophomore.classfile.Fields;
import sophomore.classfile.FieldInfo;
import sophomore.classfile.Methods;
import sophomore.classfile.MethodInfo;
import sophomore.classfile.constantpool.ConstantInfo;
import sophomore.classfile.constantpool.ConstantInfoBuilder;
import sophomore.classfile.constantpool.ConstantType;
import sophomore.classfile.constantpool.UnknownConstantTypeException;


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
		try(RandomAccessFile raf = new RandomAccessFile(new File(fileName), "r")) {
			readHeaders(raf);
			readConstantPool(raf, raf.readShort()-1);
			System.out.println(cf.pool.toString());
			readAccessFlag(raf);
			readClass(raf);
			readInterfaces(raf, raf.readShort());
			readFields(raf, raf.readShort());
			readMethods(raf, raf.readShort());
			readAttributes(raf, raf.readShort());
		} catch(IOException | UnknownConstantTypeException e) {
			e.printStackTrace();
		}
//		for(ConstantInfo info : getClassFile().cp.getPool()) {
//			System.out.println(info.toString());
//		}
	}

	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	private void readHeaders(RandomAccessFile raf) throws IOException {
		cf.magicNumber  = raf.readInt(); // 4byte
		cf.minorVersion = raf.readShort(); // 2byte
		cf.majorVersion = raf.readShort(); // 2byte
		System.out.println(cf);
	}

	/**
	 * 
	 * @param raf
	 * @throws IOException
	 * @throws UnknownConstantTypeException 
	 */
	private void readConstantPool(RandomAccessFile raf, int constantPoolCount)
	throws IOException, UnknownConstantTypeException {
		ConstantPool pool = new ConstantPool(constantPoolCount);
		for(int i = 0; i < constantPoolCount; i++) {
			int tag = raf.readByte(); // 1 byte
			ConstantInfo info = ConstantInfoBuilder.getInstance().build(tag);
			info.read(raf);
			pool.add(i, info);
			if(tag == ConstantType.C_DOUBLE || tag == ConstantType.C_LONG) {
				i++;
				pool.add(i, info);
			}
		}
		cf.pool = pool;
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
		cf.thisClass  = raf.readShort();
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
			
			cf.fields.add(i, info);
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
			cf.methods.add(i, info);
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