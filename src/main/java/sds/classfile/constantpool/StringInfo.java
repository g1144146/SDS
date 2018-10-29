package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.3">
 * Constant_String_Info</a>.
 * @author inagaki
 */
public class StringInfo implements ConstantInfo {
    final int stringIndex;

    StringInfo(int stringIndex) {
        this.stringIndex = stringIndex;
    }

    @Override
    public String toString() {
        return "CONSTANT_STRING\t#" + stringIndex;
    }
}