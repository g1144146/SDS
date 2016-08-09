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
	private String fileName;
	private byte[] bytes;
	private InputStream stream;
	private ClassFile cf;

	/**
	 * constructor.
	 * @param file classfile name
	 */
	public ClassFileReader(String file) {
		this.fileName = file;
		cf = new ClassFile();
	}

	public ClassFileReader(byte[] bytes) {
		this.bytes = bytes;
		cf = new ClassFile();
	}

	public ClassFileReader(InputStream input) {
		this.stream = input;
		cf = new ClassFile();
	}

	/**
	 * reads classfile.
	 */
	public void read() {
		try(ClassFileStream data = (fileName != null)
				? new ClassFileStream(new RandomAccessFile(new File(fileName), "r"))
				: new ClassFileStream(stream)) {
			readHeaders(data);
			readConstantPool(data, data.readShort()-1);
			readAccessFlag(data);
			readClass(data);
			readInterfaces(data, data.readShort());
			readFields(data, data.readShort());
			readMethods(data, data.readShort());
			cf.attr = readAttributes(data, data.readShort());
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
		cf.fields = new Fields(fieldCount);
		for(int i = 0; i < fieldCount; i++) {
			FieldInfo info = new FieldInfo();
			info.read(data, cf.pool);
			Attributes attr = readAttributes(data, data.readShort());
			info.setAttr(attr);
			cf.fields.add(i, info);
		}
	}

	private void readMethods(ClassFileStream data, int methodCount) throws Exception {
		cf.methods = new Methods(methodCount);
		for(int i = 0; i < methodCount; i++) {
			MethodInfo info = new MethodInfo();
			info.read(data, cf.pool);
			Attributes attr = readAttributes(data, data.readShort());
			info.setAttr(attr);
			cf.methods.add(i, info);
		}
	}

	private Attributes readAttributes(ClassFileStream data, int attrCount) throws Exception {
		Attributes attrs = new Attributes(attrCount);
		AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
		for(int i = 0; i < attrCount; i++) {
			int nameIndex = data.readShort();
			String attrName = ((Utf8Info)cf.pool.get(nameIndex-1)).getValue();
			AttributeInfo info = builder.build(attrName, nameIndex, data.readInt());
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