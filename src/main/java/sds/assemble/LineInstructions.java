package sds.assemble;

import java.util.List;
import java.util.ArrayList;
import sds.classfile.bytecode.OpcodeInfo;

/**
 * This class is for instructions of a line of method.
 * @author inagaki
 */
public class LineInstructions {
    private List<OpcodeInfo> opcodes = new ArrayList<>();

    /**
     * adds opcode.
     * @param opcode opcode 
     */
    public void add(OpcodeInfo opcode) {
        opcodes.add(opcode);
    }

    /**
     * returns opcodes of this line.
     * @return opcodes
     */
    public OpcodeInfo[] getOpcodes() {
        return opcodes.toArray(new OpcodeInfo[0]);
    }

    /**
     * returns opcode from specified pc.
     * @param pc index into the code array.
     * @return opcode
     */
    public OpcodeInfo get(int pc) {
        for(OpcodeInfo op : opcodes) {
            if(op.getPc() == pc) {
                return op;
            }
        }
        return null;
    }

    /**
     * returns values of pc of opcode.
     * @return values
     */
    public int[] getKeys() {
        return opcodes.stream().mapToInt(op -> op.getPc()).toArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(OpcodeInfo op : opcodes) {
            sb.append(op.getPc()).append(":").append(op).append("\n");
        }
        return sb.toString();
    }
}