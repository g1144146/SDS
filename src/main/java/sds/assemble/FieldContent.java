package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;
import sds.classfile.attributes.ConstantValue;

import static sds.util.Utf8ValueExtractor.extract;

/**
 * This class is for contents of field.
 * @author inagaki
 */
public class FieldContent extends MemberContent {
	private String constVal;

	/**
	 * constructor.
	 * @param info field info
	 * @param pool constant-pool
	 */
	public FieldContent(MemberInfo info, ConstantPool pool) {
		super(info, pool, Type.Field);
		for(AttributeInfo attr : info.getAttr().getAll()) {
			investigateAttribute(attr, pool);
		}
	}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		if(info.getType() == AttributeType.ConstantValue) {
			ConstantValue cv = (ConstantValue)info;
			this.constVal = extract(pool.get(cv.getConstantValueIndex()-1), pool);
		} else {
			super.investigateAttribute(info, pool);
		}
	}

	/**
	 * returns constant value of field.<br>
	 * when field type is primitive or String and field access flag doesn't contain final
	 * , this method returns null.
	 * @return constant value
	 */
	public String getConstVal() {
		return constVal;
	}
}