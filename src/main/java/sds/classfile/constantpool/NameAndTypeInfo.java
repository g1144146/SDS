package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6">
 * Constant_NameAndType_Info</a>.
 * @author inagaki
 */
public class NameAndTypeInfo implements ConstantInfo {
    final int nameIndex;
    final int descIndex;

    NameAndTypeInfo(int nameIndex, int descIndex) {
        this.nameIndex = nameIndex;
        this.descIndex = descIndex;
    }

    @Override
    public String toString() {
        return "CONSTANT_NAME_AND_TYPE\t#" + nameIndex + ":#" + descIndex;
    }
}