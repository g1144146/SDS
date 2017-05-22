package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7">
 * Constant_Utf8_Info</a>.
 * @author inagaki
 */
public class Utf8Info implements ConstantInfo {
    final String value;
    
    Utf8Info(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CONSTANT_UTF8\t" + value;
    }
}