package sds.classfile.bytecode;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.bipush">
 * bipush
 * </a>
 * and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.sipush">
 * sipush
 * </a>
 * @author inagaki
 */
public class PushOpcode extends OpcodeInfo {
    /**
     * push value.
     */
    public final int value;

    PushOpcode(int value, MnemonicTable opcodeType, int pc) {
        super(opcodeType, pc);
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PushOpcode)) {
            return false;
        }
        PushOpcode opcode = (PushOpcode)obj;
        return super.equals(obj) && (value == opcode.value);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + value;
    }
}