package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1">
 * Constant_Class_Info</a>.
 * @author inagaki
 */
public class ClassInfo implements ConstantInfo {
    /**
	 * constant entry index of class name.
	 */
	final int nameIndex;

	ClassInfo(int nameIndex) {
        this.nameIndex = nameIndex;
	}

	@Override
	public String toString() {
		return "CONSTANT_CLASS\t#" + nameIndex;
	}
}