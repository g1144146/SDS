package sds;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.Attributes;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.FieldInfo;
import sds.classfile.Methods;
import sds.classfile.MethodInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeInfoBuilder;
import sds.classfile.attributes.AttributeType;
import sds.classfile.attributes.Code;
import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.ConstantInfoBuilder;
import sds.classfile.constantpool.ConstantType;
import sds.classfile.constantpool.Utf8Info;
import sds.classfile.constantpool.ConstantTypeException;
import sds.util.ClassFilePrinter;

/**
 * This class is for reading classfile contents.
 * @author inagaki
 */
public class ClassFileReader {
	/**
	 * classfile name.
	 */
	private String fileName;
	/**
	 * classfile contents.
	 */
	private ClassFile cf;

	/**
	 * constructor.
	 * @param file classfile name
	 */
	public ClassFileReader(String file) {
		this.fileName = file;
		cf = new ClassFile();
	}

	/**
	 * reads classfile.
	 */
	public void read() {
		try(RandomAccessFile raf = new RandomAccessFile(new File(fileName), "r")) {
			readHeaders(raf);
			readConstantPool(raf, raf.readShort()-1);
			readAccessFlag(raf);
			readClass(raf);
			readInterfaces(raf, raf.readShort());
			readFields(raf, raf.readShort());
			readMethods(raf, raf.readShort());
			cf.attr = readAttributes(raf, raf.readShort());
			
			ClassFilePrinter printer = new ClassFilePrinter(cf.pool);
			printer.printNumber(cf.majorVersion, cf.minorVersion);
			printer.printConstantPool();
			printer.printAccessFlag(cf.accessFlag);
			printer.printThisClass(cf.thisClass);
			printer.printSuperClass(cf.superClass);
			printer.printInterface(cf.interfaces);
			printer.printFields(cf.fields);
			printer.printMethods(cf.methods);
			printer.printAttributes(cf.attr);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads headers of classfile.
	 * @param raf classfile stream
	 * @throws IOException
	 */
	private void readHeaders(RandomAccessFile raf) throws IOException {
		cf.magicNumber  = raf.readInt(); // 4byte
		cf.minorVersion = raf.readShort(); // 2byte
		cf.majorVersion = raf.readShort(); // 2byte
	}

	/**
	 * reads constant-pool of classfile.
	 * @param raf classfile stream
	 * @throws Exception
	 */
	private void readConstantPool(RandomAccessFile raf, int constantPoolCount) throws Exception {
		ConstantPool pool = new ConstantPool(constantPoolCount);
		ConstantInfoBuilder builder = ConstantInfoBuilder.getInstance();
		for(int i = 0; i < constantPoolCount; i++) {
			int tag = raf.readByte(); // 1 byte
			ConstantInfo info = builder.build(tag);
			info.read(raf);
			pool.add(i, info);
			if(tag == ConstantType.C_DOUBLE || tag == ConstantType.C_LONG) {
				i++;
				pool.add(i, null);
			}
		}
		cf.pool = pool;
	}

	/**
	 * reads access flag of class.
	 * @param raf classfile stream
	 * @throws IOException
	 */
	private void readAccessFlag(RandomAccessFile raf) throws IOException {
		cf.accessFlag = raf.readShort();
	}

	/**
	 * reads constant-pool entry index of class and super-class.
	 * @param raf classfile stream
	 * @throws IOException
	 */
	private void readClass(RandomAccessFile raf) throws IOException {
		cf.thisClass  = raf.readShort();
		cf.superClass = raf.readShort();
	}

	/**
	 * reads constant-pool entry indexes of interface.
	 * @param raf classfile stream
	 * @param interfaceCount interface count
	 * @throws IOException
	 */
	private void readInterfaces(RandomAccessFile raf, int interfaceCount) throws IOException {
		int[] interfaces = new int[interfaceCount];
		for(int i = 0; i < interfaceCount; i++) {
			interfaces[i] = raf.readShort();
		}
		cf.interfaces = interfaces;
	}

	/**
	 * reads fields of class.
	 * @param raf classfile stream
	 * @param fieldCount field count
	 * @throws Exception
	 */
	private void readFields(RandomAccessFile raf, int fieldCount) throws Exception {
		cf.fields = new Fields(fieldCount);
		for(int i = 0; i < fieldCount; i++) {
			FieldInfo info = new FieldInfo();
			info.read(raf);
			Attributes attr = readAttributes(raf, raf.readShort());
			info.setAttr(attr);
			cf.fields.add(i, info);
		}
	}

	/**
	 * reads methods of class.
	 * @param raf classfile stream
	 * @param methodCount method count
	 * @throws Exception
	 */
	private void readMethods(RandomAccessFile raf, int methodCount) throws Exception {
		cf.methods = new Methods(methodCount);
		for(int i = 0; i < methodCount; i++) {
			MethodInfo info = new MethodInfo();
			info.read(raf);
			Attributes attr = readAttributes(raf, raf.readShort());
			info.setAttr(attr);
			cf.methods.add(i, info);
		}
	}

	/**
	 * reads attributes of class.
	 * @param raf classfile stream
	 * @param attrCount attribute count
	 * @return attributes
	 * @throws Exception
	 */
	private Attributes readAttributes(RandomAccessFile raf, int attrCount) throws Exception {
		Attributes attrs = new Attributes(attrCount);
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		for(int i = 0; i < attrCount; i++) {
			int nameIndex = raf.readShort();
			String attrName = ((Utf8Info)cf.pool.get(nameIndex-1)).getValue();
			AttributeInfo info = builder.build(attrName, nameIndex, raf.readInt());
			if(info.getType() == AttributeType.Code) {
				((Code)info).read(raf, cf.pool);
			} else {
				info.read(raf);
			}
			attrs.add(i, info);
		}
		return attrs;
	}

	/**
	 * returns classfile contents.
	 * @return classfile
	 */
	public ClassFile getClassFile() {
		return cf;
	}
}