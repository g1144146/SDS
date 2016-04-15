package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

import static sds.classfile.constantpool.ConstantType.*;

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
abstract class NumberInfo extends ConstantInfo {
	Number number;

	NumberInfo(int tag) {
		super(tag);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException, NumberTypeException {
		switch(this.getTag()) {
			case C_INTEGER: this.number = raf.readInt();    break;
			case C_FLOAT:   this.number = raf.readFloat();  break;
			case C_LONG:    this.number = raf.readLong();   break;
			case C_DOUBLE:  this.number = raf.readDouble(); break;
			default: throw new NumberTypeException("unknown number type: " + this.getTag());
		}
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
