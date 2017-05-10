package sds.classfile.bytecode;

/**
 * This class is for opcode has index operand.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iload">
 * iload
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lload">
 * lload
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fload">
 * fload
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dload">
 * dload
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aload">
 * aload
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.istore">
 * istore
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lstore">
 * lstore
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fstore">
 * fstore
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.store">
 * dstore
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.astore">
 * astore
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ret">
 * ret
 * </a>.
 * @author inagakikenichi
 */
public class IndexOpcode extends OpcodeInfo {
    private int index;

    IndexOpcode(int index, MnemonicTable opcodeType, int pc) {
        super(opcodeType, pc);
        this.index = index;
    }

    /**
     * returns index.
     * @return index
     */
    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IndexOpcode)) {
            return false;
        }
        IndexOpcode opcode = (IndexOpcode)obj;
        return super.equals(obj) && (index == opcode.index);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + index;
    }
}