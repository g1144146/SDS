package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.wide">
 * wide
 * </a>.
 * @author inagaki
 */
public class Wide extends OpcodeInfo {
    private int constByte = -1;
    private CpRefOpcode ref;

    Wide(ClassFileStream data, ConstantInfo[] pool, int pc) throws IOException {
        super(MnemonicTable.wide, pc);
        int tag = data.readByte();
        if(data.readByte() == MnemonicTable.ldc.getOpcode()) {
            this.ref = new CpRefOpcode(data.readUnsignedByte(), pool, MnemonicTable.values()[tag], pc);
        } else {
            this.ref = new CpRefOpcode(data.readShort(), pool, MnemonicTable.values()[tag], pc);
        }
        this.constByte = (tag == MnemonicTable.iinc.getOpcode()) ? data.readShort() : -1;
    }

    /**
     * returns const.<br>
     * if opcode item of this opcode is {@link Wide <code>Wide</code>}, const value equals -1.
     * @return const
     */
    public int getConst() {
        return constByte;
    }

    /**
     * returns constant-pool entry index.
     * @return constant-pool entry index.
     */
    public int getIndexByte() {
        return ref.index;
    }

    /**
     * returns operand which this opcode has.
     * @return operand.
     */
    public String getOperand() {
        return ref.operand;
    }

    /**
     * returns type.<br>
     * returns 0-length string in case of opcode type is not ldc, ldc_w or ldc2_w.
     * @return type
     */
    public String getLDCType() {
        return ref.type;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Wide)) {
            return false;
        }
        Wide opcode = (Wide)obj;
        return super.equals(obj) && (constByte == opcode.constByte);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + constByte + ", " + ref;
    }
}