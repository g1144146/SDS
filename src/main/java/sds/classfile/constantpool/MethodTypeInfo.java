package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.9">
 * Constant_MethodType_Info</a>.
 * @author inagaki
 */
public class MethodTypeInfo extends ConstantInfo {
    private int descIndex;

    MethodTypeInfo(int descIndex) {
        super(ConstantType.C_METHOD_TYPE);
        this.descIndex = descIndex;
    }

    /**
     * returns constant-pool entry index of descriptor of method type.
     * @return constant-pool entry index of descriptor of method type
     */
    public int getDescIndex() {
        return descIndex;
    }

    @Override
    public String toString() {
        return super.toString() + "\t#" + descIndex;
    }
}