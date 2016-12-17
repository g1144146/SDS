package sds.classfile.attributes;

import sds.classfile.Info;
import sds.classfile.ConstantPool;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This adapter class is for info of class has attribute.
 * @author inagaki
 */
public abstract class AttributeInfo implements Info {
	private AttributeType type;

	/**
	 * constructor.
	 * @param type attribute type
	 */
	public AttributeInfo(AttributeType type) {
		this.type = type;
	}

	/**
	 * returns extracted content of the attribute from constant-pool.
	 * @param info constant info which the attribute has
	 * @param pool constant-pool
	 * @return extracted content
	 */
	public String extract(ConstantInfo info, ConstantPool pool) {
		return sds.util.Utf8ValueExtractor.extract(info, pool);
	}

	/**
	 * returns attribute type.
	 * @return type
	 */
	public AttributeType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}