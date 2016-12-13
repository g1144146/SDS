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
	 * returns constant-pool entry index of attribute name.
	 * @return constant-pool entry index of attribute name
	 */
//	public int getNameIndex() {
//		return nameIndex;
//	}

	/**
	 * returns attribute type.
	 * @return type
	 */
	public AttributeType getType() {
		return type;
	}
}