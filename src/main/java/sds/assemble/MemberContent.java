package sds.assemble;

import sds.classfile.MemberInfo;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This adapter class is for
 * {@link FieldContent <code>FieldContent</code>}
 * and
 * {@link MethodContent <code>MethodContent</code>}.
 * @author inagaki
 */
public abstract class MemberContent extends BaseContent {
    /**
     * this member's access flag.
     */
    public final String accessFlag;
    /**
     * this member's name.
     */
    public final String name;
    String desc;

    MemberContent(MemberInfo info, ConstantInfo[] pool) {
        this.accessFlag = info.getAccessFlags();
        this.desc = info.getDescriptor();
        this.name = info.getName();
    }

    /**
     * returns this member's descriptor.
     * @return descriptor
     */
    public String getDescriptor() {
        return desc;
    }
}