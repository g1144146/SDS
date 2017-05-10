package sds.classfile.attributes;

import java.io.IOException;
import java.util.StringJoiner;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.5">
 * Exceptions Attribute</a>.
 * @author inagaki
 */
public class Exceptions extends AttributeInfo {
    private String[] exceptionTable;

    Exceptions(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.Exceptions);
        this.exceptionTable = new String[data.readShort()];
        for(int i = 0; i < exceptionTable.length; i++) {
            exceptionTable[i] = extract(data.readShort(), pool).replace("/", ".");
        }
    }

    /**
     * returns exception classes.
     * @return exception classes
     */
    public String[] getExceptionTable() {
        return exceptionTable;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for(String ex : exceptionTable) {
            sj.add(ex);
        }
        return super.toString() + ": " + sj.toString();
    }
}