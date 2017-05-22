package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10">
 * Constant_InvokeDynamic_Info</a>.
 * @author inagaki
 */
public class InvokeDynamicInfo implements ConstantInfo {
    final int bsmAttrIndex;
    final int nameAndTypeIndex;

    InvokeDynamicInfo(int bsmAttrIndex, int nameAndTypeIndex) {
        this.bsmAttrIndex = bsmAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {
        return "CONSTANT_INVOKE_DYNAMIC\t#" + bsmAttrIndex + ":#" + nameAndTypeIndex;
    }
}