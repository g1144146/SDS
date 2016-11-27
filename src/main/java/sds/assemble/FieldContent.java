package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.ConstantValue;
import sds.classfile.attributes.Signature;

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
		super(info, pool);
		for(AttributeInfo attr : info.getAttr().getAll()) {
			analyzeAttribute(attr, pool);
		}
	}

	@Override
	public void analyzeAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case ConstantValue:
				ConstantValue cv = (ConstantValue)info;
				this.constVal = cv.getConstantValue();
				break;
			case Signature:
				Signature sig = (Signature)info;
				this.desc = sig.getSignature();
				break;
			default:
				super.analyzeAttribute(info, pool);
				break;
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