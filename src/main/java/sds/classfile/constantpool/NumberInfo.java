package sds.classfile.constantpool;

import java.io.IOException;
import java.io.RandomAccessFile;

import static sds.classfile.constantpool.ConstantType.*;

/**
 *
 * @author inagakikenichi
 */
abstract class NumberInfo extends ConstantInfo {
	Number number;

	NumberInfo(int tag) {
		super(tag);
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException, NumberTypeException {
		switch(tag) {
			case C_INTEGER: this.number = raf.readInt();    break;
			case C_FLOAT:   this.number = raf.readFloat();  break;
			case C_LONG:    this.number = raf.readLong();   break;
			case C_DOUBLE:  this.number = raf.readDouble(); break;
			default: throw new NumberTypeException("unknown number type: " + tag);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("\t");
		switch(tag) {
			case C_INTEGER: sb.append(number.intValue());      break;
			case C_FLOAT:   sb.append(number.floatValue());    break;
			case C_LONG:    sb.append(number.longValue());     break;
			case C_DOUBLE:  sb.append(number.doubleValue());   break;
			default:        sb.append("unknown number type."); break;
		}
		return sb.toString();
	}
}
