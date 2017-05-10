package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.3">
 * Constant_String_Info</a>.
 * @author inagaki
 */
public class StringInfo extends ConstantInfo {
    private int stringIndex;

    StringInfo(int stringIndex) {
        super(ConstantType.C_STRING);
        this.stringIndex = stringIndex;
    }

    /**
     * returns constant-pool entry index of string.
     * @return constant-pool entry index of string
     */
    public int getStringIndex() {
        return stringIndex;
    }

    @Override
    public String toString() {
        return super.toString() + "\t#" + stringIndex;
    }
}