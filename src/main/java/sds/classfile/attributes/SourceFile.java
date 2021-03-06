package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.10">
 * SourceFile Attribute</a>.
 * @author inagaki
 */
public class SourceFile extends AttributeInfo {
	private String sourceFile;

	/**
	 * constructor.
	 */
	public SourceFile() {
		super(AttributeType.SourceFile);
	}

	/**
	 * returns source file.
	 * @return source file
	 */
	public String getSourceFile() {
		return sourceFile;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.sourceFile = extract(pool.get(data.readShort()-1), pool);
	}
}