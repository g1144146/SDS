package sds.classfile.bytecode;

import sds.classfile.Info;

/**
 * This class is for opcode has no operand.
 * @author inagaki
 */
public class OpcodeInfo implements Info {
    int pc;
    MnemonicTable opcodeType;

    OpcodeInfo(MnemonicTable opcodeType, int pc) {
        this.opcodeType = opcodeType;
        this.pc = pc;
    }

    /**
     * returns opcode type.
     * @return opcode type.
     */
    public MnemonicTable getType() {
        return opcodeType;
    }

    /**
     * returns index into the code array.
     * @return index
     */
    public int getPc() {
        return pc;
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