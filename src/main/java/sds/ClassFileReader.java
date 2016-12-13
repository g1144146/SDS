package sds;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.Attributes;
import sds.classfile.ConstantPool;
import sds.classfile.ClassFileStream;
import sds.classfile.Fields;
import sds.classfile.FieldInfo;
import sds.classfile.Methods;
import sds.classfile.MethodInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeInfoBuilder;
import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.ConstantInfoBuilder;
import sds.classfile.constantpool.ConstantType;
import sds.classfile.constantpool.Utf8Info;

/**
 * This class is for reading classfile contents.
 * @author inagaki
 */
public class ClassFileReader {
	private ClassFileStream stream;
	private ClassFile cf;

	/**
	 * constructor for classfile.
	 * @param fileName classfile name
	 */
	public ClassFileReader(String fileName) {
		try {
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			this.stream = new ClassFileStream(raf);
		} catch(IOException e) {
			e.printStackTrace();
		}
		cf = new ClassFile();
	}

	/**
	 * constructor for jar file.
	 * @param input 
	 */
	public ClassFileReader(InputStream input) {
		try {
			this.stream = new ClassFileStream(input);
		} catch(IOException e) {
			e.printStackTrace();
		}
		cf = new ClassFile();
	}

	/**
	 * reads classfile.
	 */
	public void read() {
		try {
			readHeaders(stream);
			readConstantPool(stream, stream.readShort()-1);
			readAccessFlag(stream);
			readClass(stream);
			readInterfaces(stream, stream.readShort());
			readFields(stream, stream.readShort());
			readMethods(stream, stream.readShort());
			cf.attr = readAttributes(stream, stream.readShort());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void readHeaders(ClassFileStream data) throws IOException {
		cf.magicNumber  = data.readInt(); // 4byte
		cf.minorVersion = data.readShort(); // 2byte
		cf.majorVersion = data.readShort(); // 2byte
	}

	private void readConstantPool(ClassFileStream data, int constantPoolCount) throws Exception {
		ConstantPool pool = new ConstantPool(constantPoolCount);
		ConstantInfoBuilder builder = ConstantInfoBuilder.getInstance();
		for(int i = 0; i < constantPoolCount; i++) {
			int tag = data.readByte(); // 1 byte
			ConstantInfo info = builder.build(tag);
			info.read(data);
			pool.add(i, info);
			if(tag == ConstantType.C_DOUBLE || tag == ConstantType.C_LONG) {
				i++;
				pool.add(i, null);
			}
		}
		cf.pool = pool;
	}

	private void readAccessFlag(ClassFileStream data) throws IOException {
		cf.accessFlag = data.readShort();
	}

	private void readClass(ClassFileStream data) throws IOException {
		cf.thisClass  = data.readShort();
		cf.superClass = data.readShort();
	}

	private void readInterfaces(ClassFileStream data, int interfaceCount) throws IOException {
		int[] interfaces = new int[interfaceCount];
		for(int i = 0; i < interfaceCount; i++) {
			interfaces[i] = data.readShort();
		}
		cf.interfaces = interfaces;
	}

	private void readFields(ClassFileStream data, int fieldCount) throws Exception {
		Fields fields = new Fields(fieldCount);
		Attributes attr;
		for(int i = 0; i < fieldCount; i++) {
			FieldInfo info = new FieldInfo();
			info.read(data, cf.pool);
			attr = readAttributes(data, data.readShort());
			info.setAttr(attr);
			fields.add(i, info);
		}
		cf.fields = fields;
	}

	private void readMethods(ClassFileStream data, int methodCount) throws Exception {
		Methods methods = new Methods(methodCount);
		Attributes attr;
		for(int i = 0; i < methodCount; i++) {
			MethodInfo info = new MethodInfo();
			info.read(data, cf.pool);
			attr = readAttributes(data, data.readShort());
			info.setAttr(attr);
			methods.add(i, info);
		}
		cf.methods = methods;
	}

	private Attributes readAttributes(ClassFileStream data, int attrCount) throws Exception {
		Attributes attrs = new Attributes(attrCount);
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		AttributeInfo info;
		Utf8Info utf8Info;
		ConstantPool pool = cf.pool;
		for(int i = 0; i < attrCount; i++) {
			int nameIndex = data.readShort();
			utf8Info = (Utf8Info)pool.get(nameIndex-1);
			String attrName = utf8Info.getValue();
			info = builder.build(attrName, nameIndex, data.readInt());
			info.read(data, cf.pool);
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