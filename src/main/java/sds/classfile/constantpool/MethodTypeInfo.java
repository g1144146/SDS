package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.9">
 * Constant_MethodType_Info</a>.
 * @author inagaki
 */
public class MethodTypeInfo implements ConstantInfo {
    final int descIndex;

    MethodTypeInfo(int descIndex) {
        this.descIndex = descIndex;
    }

    @Override
    public String toString() {
        return "CONSTANT_METHOD_TYPE\t#" + descIndex;
    }
}