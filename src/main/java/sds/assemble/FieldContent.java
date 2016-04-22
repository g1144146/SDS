package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;
import sds.classfile.attributes.ConstantValue;

/**
 * This class is for contents of field.
 * @author inagaki
 */
public class FieldContent extends MemberContent {
	/**
	 * constructor.
	 * @param info field info
	 * @param pool constant-pool
	 */
	public FieldContent(MemberInfo info, ConstantPool pool) {
		super(info, pool);
		for(AttributeInfo attr : info.getAttr().getAll()) {
			investigateAttribute(attr, pool);
		}
	}

	@Override
	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		if(info.getType() == AttributeType.ConstantValue) {
			ConstantValue cv = (ConstantValue)info;
		} else {
			super.investigateAttribute(info, pool);
		}
	}
}