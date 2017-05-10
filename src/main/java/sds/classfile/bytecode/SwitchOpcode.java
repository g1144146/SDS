package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This adapter class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lookupswitch">
 * lookupswitch
 * </a> and 
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.tableswitch">
 * tableswitch
 * </a>.
 * @author inagaki
 */
public abstract class SwitchOpcode extends OpcodeInfo {
    private int defaultByte;

    SwitchOpcode(ClassFileStream data, MnemonicTable opcodeType, int pc) throws IOException {
        super(opcodeType, pc);
        for(int i = 1; ((i + pc) % 4) != 0; i++) {
            data.readByte();
        }
        this.defaultByte = data.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SwitchOpcode)) {
            return false;
        }
        SwitchOpcode opcode = (SwitchOpcode)obj;
        return super.equals(obj) && (defaultByte == opcode.defaultByte);
    }

    /**
     * returns default byte.<br>
     * jump point of default key is "defaultByte + pc".
     * @return default byte
     */
    public int getDefault() {
        return defaultByte;
    }
}