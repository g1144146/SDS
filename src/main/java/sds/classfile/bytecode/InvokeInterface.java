package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokeinterface">
 * invokeinterface
 * </a>.
 * @author inagaki
 */
public class InvokeInterface extends CpRefOpcode {
    /**
     * count.
     */
    public final int count;

    InvokeInterface(ClassFileStream data, ConstantInfo[] pool, int pc) throws IOException {
        super(data.readShort(), pool, MnemonicTable.invokeinterface, pc);
        this.count = data.readUnsignedByte();
        data.skipBytes(1);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof InvokeInterface)) {
            return false;
        }
        InvokeInterface opcode = (InvokeInterface)obj;
        return super.equals(obj) && (count == opcode.count);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + count;
    }
}