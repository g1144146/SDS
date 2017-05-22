package sds.classfile.attributes;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.15">
 * Deprecated Attribute</a>.
 * @author inagaki
 */
public class Deprecated implements AttributeInfo {
    @Override
    public String toString() { return "[Deprecated]"; }
}