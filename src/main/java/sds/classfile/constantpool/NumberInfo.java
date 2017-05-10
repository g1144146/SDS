package sds.classfile.constantpool;

import static sds.classfile.constantpool.ConstantType.C_INTEGER;
import static sds.classfile.constantpool.ConstantType.C_LONG;
import static sds.classfile.constantpool.ConstantType.C_FLOAT;
import static sds.classfile.constantpool.ConstantType.C_DOUBLE;

/**
 * This adapter class is for
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
public abstract class NumberInfo extends ConstantInfo {
	Number number;

	NumberInfo(int tag, Number number) {
		super(tag);
        this.number = number;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t");
		switch(this.getTag()) {
			case C_INTEGER: sb.append(number.intValue());      break;
			case C_FLOAT:   sb.append(number.floatValue());    break;
			case C_LONG:    sb.append(number.longValue());     break;
			case C_DOUBLE:  sb.append(number.doubleValue());   break;
			default:        sb.append("unknown number type."); break;
		}
		return sb.toString();
	}
}

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
 * Constant_Integer_Info</a>.
 */
class IntInfo extends NumberInfo {
    IntInfo(int value) {
        super(C_INTEGER, value);
    }
    
    /**
     * returns int value.
     * @return value
     */
    public int getValue() {
        return this.number.intValue();
    }
}


/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4">
 * Constant_Float_Info</a>.
 */
class FloatInfo extends NumberInfo {
    FloatInfo(float value) {
        super(C_FLOAT, value);
    }
    
    /**
     * returns float value.
     * @return value
     */
    public float getValue() {
        return this.number.floatValue();
    }
}

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
 * Constant_Long_Info</a>.
 * @author inagaki
 */
class LongInfo extends NumberInfo {
    LongInfo(long value) {
        super(C_LONG, value);
    }
    
    /**
     * returns long value.
     * @return value
     */
    public long getValue() {
        return this.number.longValue();
    }
}

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5">
 * Constant_Double_Info</a>.
 * @author inagaki
 */
class DoubleInfo extends NumberInfo {
    DoubleInfo(double value) {
        super(C_DOUBLE, value);
    }
    
    /**
     * returns double value.
     * @return value
     */
    public double getValue() {
        return this.number.doubleValue();
    }
}