package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.tableswitch">
 * tableswitch
 * </a>.
 * @author inagaki
 */
public class TableSwitch extends SwitchOpcode {
    /**
     * jump offsets.<br>
     * jump point of each case keyword is "jump offset + pc".
     */
    public final int[] jumpOffsets;

    TableSwitch(ClassFileStream data, int pc) throws IOException {
        super(data, MnemonicTable.tableswitch, pc);
        int low = data.readInt();
        int high = data.readInt();
        this.jumpOffsets = new int[high - low + 1];
        for(int i = 0; i < jumpOffsets.length; i++) {
            jumpOffsets[i] = data.readInt();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TableSwitch)) {
            return false;
        }
        TableSwitch opcode = (TableSwitch)obj;
        return super.equals(obj) && java.util.Arrays.equals(jumpOffsets, opcode.jumpOffsets);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < jumpOffsets.length; i++) {
            sb.append(jumpOffsets[i] + pc).append("\n");
        }
        sb.append(getDefault() + pc);
        return super.toString() + ": " + sb.toString();
    }
}