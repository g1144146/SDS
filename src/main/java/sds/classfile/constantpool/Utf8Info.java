package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7">
 * Constant_Utf8_Info</a>.
 * @author inagaki
 */
public class Utf8Info extends ConstantInfo {
    private String value;
    
    Utf8Info(String value) {
        super(ConstantType.C_UTF8);
        this.value = value;
    }

    /**
     * returns string of this constant info has.
     * @return string
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString() + "\t" + value;
    }
}