package sds.classfile.bytecode;

import sds.classfile.Info;

/**
 * This class is for opcode has no operand.
 * @author inagaki
 */
public class OpcodeInfo implements Info {
    /**
     * index into the code array.
     */
    public final int pc;
    /**
     * opcode type.
     */
    public final MnemonicTable opcodeType;

    OpcodeInfo(MnemonicTable opcodeType, int pc) {
        this.opcodeType = opcodeType;
        this.pc = pc;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof OpcodeInfo)) {
            return false;
        }
        OpcodeInfo opcode = (OpcodeInfo)obj;
        return opcodeType == opcode.opcodeType;
    }

    @Override
    public String toString() {
        return opcodeType.toString();
    }
}