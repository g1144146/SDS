package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.2">
 * ConstantValue Attribute</a>.
 * @author inagaki
 */
public class ConstantValue extends AttributeInfo {
	private String constantValue;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public ConstantValue(int nameIndex, int length) {
		super(AttributeType.ConstantValue, nameIndex, length);
	}

	/**
	 * returns constant value.
	 * @return constant value
	 */
	public String getConstantValue() {
		return constantValue;
	}

	@Override
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		int constantValueIndex = data.readShort();
		this.constantValue = extract(pool.get(constantValueIndex-1), pool);
	}
}