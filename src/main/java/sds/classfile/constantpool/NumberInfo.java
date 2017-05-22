package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
 * Constant_Integer_Info</a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
 * Constant_Float_Info</a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
 * Constant_Long_Info</a> and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
 * Constant_Double_Info</a>.
 * @author inagakikenichi
 */
public class NumberInfo implements ConstantInfo {
	final Number number;
    public  final int tag;

	NumberInfo(int tag, Number number) {
        this.number = number;
        this.tag = tag;
	}

	@Override
	public String toString() {
		switch(tag) {
			case ConstantInfoFactory.C_INTEGER: return "CONSTANT_INTEGER" + number.toString();
			case ConstantInfoFactory.C_FLOAT:   return "CONSTANT_FLOAT"   + number.toString();
			case ConstantInfoFactory.C_LONG:    return "CONSTANT_LONG"    + number.toString();
			case ConstantInfoFactory.C_DOUBLE:  return "CONSTANT_DOUBLE"  + number.toString();
            default: throw new IllegalStateException();
		}
	}
}