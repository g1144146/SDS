package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.7">
 * EnclosingMethod Attribute</a>.
 * @author inagaki
 */
public class EnclosingMethod extends AttributeInfo {
	private String _class;
	private String method;
	
	/**
	 * constructor.
	 */
	public EnclosingMethod() {
		super(AttributeType.EnclosingMethod);
	}

	/**
	 * returns class of enclosing method.
	 * @return class
	 */
	public String getEncClass() {
		return _class;
	}

	/**
	 * returns enclosing method.
	 * @return method
	 */
	public String getEncMethod() {
		return method;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		int classIndex  = data.readShort();
		this._class = extract(pool.get(classIndex-1), pool);
		int methodIndex = data.readShort();
		this.method = methodIndex > 0 ? extract(pool.get(methodIndex-1), pool) : "";
	}
}