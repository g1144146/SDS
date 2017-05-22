package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Fieldref_Info</a>, 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_Methodref_Info</a> and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.2">
 * Constant_InterfaceMethodref_Info</a>.
 * @author inagaki
 */
public class MemberRefInfo implements ConstantInfo {
    final int classIndex;
    final int nameAndTypeIndex;
    private final int tag;

    MemberRefInfo(int tag, int classIndex, int nameAndTypeIndex) {
        this.tag = tag;
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {
        String values = classIndex + ".#" + nameAndTypeIndex;
        if(tag == ConstantInfoFactory.C_FIELDREF)  return "CONSTANT_FIELDREF\t#"  + values;
        if(tag == ConstantInfoFactory.C_METHODREF) return "CONSTANT_METHODREF\t#" + values;
        return "CONSTANT_INTERFACE_METHODREF\t#" + values;
    }
}