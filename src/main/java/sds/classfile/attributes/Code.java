package sds.classfile.attributes;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import sds.classfile.ClassFileStream;
import sds.classfile.bytecode.MnemonicTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3">
 * Code Attribute</a>.
 * @author inagaki
 */
public class Code implements AttributeInfo {
    /**
     * maximum depth of the operand stack of method.
     */
    public final int maxStack;
    /**
     * number of local variables.
     */
    public final int maxLocals;
    /**
     * opcode sequence of method.
     */
    public final OpcodeInfo[] opcodes;
    /**
     * exception's valid ranges of method.<br>
     * when one of array index defines N, the array content is next:<br>
     * - table[N][0]: start pc<br>
     * - table[N][1]: end_pc<br>
     * - table[N][2]: handler_pc
     */
    public final int[][] exceptionTable;
    /**
     * catched exceptions of method.
     */
    public final String[] catchTable;
    /**
     * attributes of method.
     */
    public final AttributeInfo[] attr;

    Code(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.maxStack = data.readShort();
        this.maxLocals = data.readShort();

        this.opcodes = readOpcode(data, pool);
        int len = data.readShort();
        this.exceptionTable = new int[len][3];
        this.catchTable = new String[len];
        for(int i = 0; i < len; i++) {
            exceptionTable[i][0] = data.readShort();
            exceptionTable[i][1] = data.readShort();
            exceptionTable[i][2] = data.readShort();
            int catchType = data.readShort();
            catchTable[i] = (catchType == 0) ? "any" : extract(catchType, pool);
        }
        this.attr = readAttributes(data, pool);
    }

    private OpcodeInfo[] readOpcode(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        List<OpcodeInfo> list = new ArrayList<>();
        int codeLen = data.readInt();
        int filePointer = (int)data.getFilePointer();
        int index;
        while((index = (int)data.getFilePointer()) < codeLen + filePointer) {
            int pc = (index - filePointer);
            list.add(MnemonicTable.get(data, pool, pc));
        }
        return list.toArray(new OpcodeInfo[0]);
    }

    private AttributeInfo[] readAttributes(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        AttributeInfo[] attr = new AttributeInfo[data.readShort()];
        AttributeInfoFactory factory = new AttributeInfoFactory();
        for(int i = 0; i < attr.length; i++) {
            String value = extract(data.readShort(), pool);
            attr[i] = factory.create(value, data, pool, opcodes);
        }
        return attr;
    }
}