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
    private String accessFlag;
    private String name;
    String desc;

    MemberContent(MemberInfo info, ConstantInfo[] pool) {
        this.accessFlag = info.getAccessFlags();
        this.desc = info.getDescriptor();
        this.name = info.getName();
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