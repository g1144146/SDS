package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.util.AccessFlags;
import sds.util.Utf8ValueExtractor;

/**
 *
 * @author inagaki
 */
public abstract class MemberContent extends BaseContent {
	String accessFlag;
	String desc;
	String name;
	MemberContent(MemberInfo info, ConstantPool pool) {
		this.accessFlag = AccessFlags.get(info.getAccessFlags(), info.getType());
		this.desc = Utf8ValueExtractor.extract(pool.get(info.getDescriptorIndex()-1), pool);
		this.name = Utf8ValueExtractor.extract(pool.get(info.getNameIndex()-1), pool);
	}
}
