package sds.classfile.attributes;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.8">Synthetic Attribute</a>.
 * @author inagaki
 */
public class Synthetic extends AttributeInfo {
    Synthetic() {
        super(AttributeType.Synthetic);
    }
}