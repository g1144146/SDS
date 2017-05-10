package sds.classfile.bytecode;

import java.io.IOException;
import java.util.Arrays;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lookupswitch">
 * lookupswitch
 * </a>.
 * @author inagaki
 */
public class LookupSwitch extends SwitchOpcode {
    private int[] match;
    private int[] offset;

    LookupSwitch(ClassFileStream data, int pc) throws IOException {
        super(data, MnemonicTable.lookupswitch, pc);
        this.match  = new int[data.readInt()];
        this.offset = new int[match.length];
        for(int i = 0; i < match.length; i++) {
            match[i] = data.readInt();
            offset[i] = data.readInt();
        }
    }

    /**
     * returns values of case keyword.
     * @return values
     */
    public int[] getMatch() {
        return match;
    }

    /**
     * returns offsets.<br>
     * jump point of each case keyword is "offset + pc".
     * @return offsets
     */
    public int[] getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LookupSwitch)) {
            return false;
        }
        LookupSwitch opcode = (LookupSwitch)obj;
        boolean flag = true;
        flag &= Arrays.equals(match,  opcode.match);
        flag &= Arrays.equals(offset, opcode.offset);
        return super.equals(obj) && flag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < match.length; i++) {
            sb.append(match[i]).append(", ")
                .append(offset[i]+getPc()).append("\n");
        }
        sb.append(getDefault()+getPc());
        return super.toString() + ": " + sb.toString();
    }
}