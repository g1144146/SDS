package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import static sds.util.AccessFlags.get;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This adapter class is for
 * {@link FieldContent <code>FieldContent</code>}
 * and
 * {@link MethodContent <code>MethodContent</code>}.
 * @author inagaki
 */
public abstract class MemberContent extends BaseContent {
	private String accessFlag;
	private String desc;
	private String name;

	MemberContent(MemberInfo info, ConstantPool pool, Type contentType) {
		this.accessFlag = get(info.getAccessFlags(), info.getType());
		this.desc = extract(pool.get(info.getDescriptorIndex()-1), pool);
		this.name = extract(pool.get(info.getNameIndex()-1), pool);
		this.contentType = contentType;
	}

	/**
	 * returns this member's access flag.
	 * @return access flag.
	 */
	public String getAccessFlag() {
		return accessFlag;
	}

	/**
	 * returns this member's descriptor.
	 * @return descriptor
	 */
	public String getDescriptor() {
		return desc;
	}

	/**
	 * returns this member's name.
	 * @return name
	 */
	public String getName() {
		return name;
	}
}
