package sds.classfile.attributes;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import sds.classfile.ClassFileStream;
import sds.classfile.bytecode.MnemonicTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.Utf8Info;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 * This class is for <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3">
 * Code Attribute</a>.
 * @author inagaki
 */
public class Code extends AttributeInfo {
    private int maxStack;
    private int maxLocals;
    private OpcodeInfo[] opcodes;
    private int[][] exceptionTable;
    private String[] catchTable;
    private AttributeInfo[] attr;

    Code(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.Code);
        this.maxStack = data.readShort();
        this.maxLocals = data.readShort();

        readOpcode(data, pool);
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
        readAttributes(data, pool);
    }

    private void readOpcode(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        List<OpcodeInfo> list = new ArrayList<>();
        int codeLen = data.readInt();
        int filePointer = (int)data.getFilePointer();
        int index;
        while((index = (int)data.getFilePointer()) < codeLen + filePointer) {
            int pc = (index - filePointer);
            list.add(MnemonicTable.get(data, pool, pc));
        }
        this.opcodes = list.toArray(new OpcodeInfo[0]);
    }

    private void readAttributes(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        this.attr = new AttributeInfo[data.readShort()];
        AttributeInfoFactory factory = new AttributeInfoFactory();
        for(int i = 0; i < attr.length; i++) {
            String value = extract(data.readShort(), pool);
            attr[i] = factory.create(value, data, pool, opcodes);
        }
    }

    /**
     * returns maximum depth of the operand stack of method.
     * @return maximum depth.
     */
    public int getMaxStack() {
        return maxStack;
    }

    /**
     * returns number of local variables.
     * @return number of local variables.
     */
    public int maxLocals() {
        return maxLocals;
    }

    /**
     * returns opcode sequence of method.
     * @return opcode sequence
     */
    public OpcodeInfo[] getCode() {
        return opcodes;
    }

    /**
     * returns exception's valid ranges of method.<br>
     * when one of array index defines N, the array content is next:<br>
     * - table[N][0]: start pc<br>
     * - table[N][1]: end_pc<br>
     * - table[N][2]: handler_pc
     * @return exception's valid ranges
     */
    public int[][] getExceptionTable() {
        return exceptionTable;
    }

    /**
     * returns catched exceptions of method
     * @return 
     */
    public String[] getCatchTable() {
        return catchTable;
    }

    /**
     * returns attributes of method.
     * @return attributes
     */
    public AttributeInfo[] getAttr() {
        return attr;
    }
}