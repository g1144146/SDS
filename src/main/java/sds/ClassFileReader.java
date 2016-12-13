package sds;

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
			readHeaders();
			readConstantPool();
			readAccessFlag();
			readClass();
			readInterfaces();
			readFields();
			readMethods();
			cf.attr = readAttributes();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void readHeaders() throws IOException {
		cf.magicNumber = stream.readInt(); // 4byte
		cf.minorVersion = stream.readShort(); // 2byte
		cf.majorVersion = stream.readShort(); // 2byte
	}

	private void readConstantPool() throws Exception {
		int constantPoolCount = stream.readShort() - 1;
		ConstantPool pool = new ConstantPool(constantPoolCount);
		ConstantInfoBuilder builder = ConstantInfoBuilder.getInstance();
		for(int i = 0; i < constantPoolCount; i++) {
			int tag = stream.readByte(); // 1 byte
			ConstantInfo info = builder.build(tag);
			info.read(stream);
			pool.add(i, info);
			if(tag == ConstantType.C_DOUBLE || tag == ConstantType.C_LONG) {
				i++;
				pool.add(i, null);
			}
		}
		cf.pool = pool;
	}

	private void readAccessFlag() throws IOException {
		cf.accessFlag = stream.readShort();
	}

	private void readClass() throws IOException {
		cf.thisClass = stream.readShort();
		cf.superClass = stream.readShort();
	}

	private void readInterfaces() throws IOException {
		int interfaceCount = stream.readShort();
		int[] interfaces = new int[interfaceCount];
		for(int i = 0; i < interfaceCount; i++) {
			interfaces[i] = stream.readShort();
		}
		cf.interfaces = interfaces;
	}

	private void readFields() throws Exception {
		int fieldCount = stream.readShort();
		Fields fields = new Fields(fieldCount);
		Attributes attr;
		for(int i = 0; i < fieldCount; i++) {
			FieldInfo info = new FieldInfo();
			info.read(stream, cf.pool);
			attr = readAttributes();
			info.setAttr(attr);
			fields.add(i, info);
		}
		cf.fields = fields;
	}

	private void readMethods() throws Exception {
		int methodCount = stream.readShort();
		Methods methods = new Methods(methodCount);
		Attributes attr;
		for(int i = 0; i < methodCount; i++) {
			MethodInfo info = new MethodInfo();
			info.read(stream, cf.pool);
			attr = readAttributes();
			info.setAttr(attr);
			methods.add(i, info);
		}
		cf.methods = methods;
	}

	private Attributes readAttributes() throws Exception {
		int attrCount = stream.readShort();
		Attributes attrs = new Attributes(attrCount);
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		AttributeInfo info;
		Utf8Info utf8Info;
		ConstantPool pool = cf.pool;
		for(int i = 0; i < attrCount; i++) {
			int nameIndex = stream.readShort();
			utf8Info = (Utf8Info) pool.get(nameIndex - 1);
			String attrName = utf8Info.getValue();
			info = builder.build(attrName, nameIndex, stream.readInt());
			info.read(stream, cf.pool);
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