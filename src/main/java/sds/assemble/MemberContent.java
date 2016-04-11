package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.util.AccessFlags;
import sds.util.ClassFilePrinter;

/**
 *
 * @author inagaki
 */
public abstract class MemberContent extends BaseContent {
	String accessFlag;
	String desc;
	String name;
	MemberContent(MemberInfo info, ConstantPool pool) {
		ClassFilePrinter cfp = new ClassFilePrinter();
		this.accessFlag = AccessFlags.get(info.getAccessFlags(), info.getType());
		this.desc = cfp.getUtf8Value(pool.get(info.getDescriptorIndex()-1));
		this.name = cfp.getUtf8Value(pool.get(info.getNameIndex()-1));
	}
}
