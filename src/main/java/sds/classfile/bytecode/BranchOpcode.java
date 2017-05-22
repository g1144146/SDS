package sds.classfile.bytecode;

import static sds.classfile.bytecode.MnemonicTable._goto;
import static sds.classfile.bytecode.MnemonicTable.goto_w;
import static sds.classfile.bytecode.MnemonicTable.jsr;
import static sds.classfile.bytecode.MnemonicTable.jsr_w;

 /**
 * This class is for opcode has branch.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_cond">
 * ifeq, ifne, iflt, ifge, ifgt, ifle
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmp_cond">
 * if_icmpeq, if_icmpne, if_icmplt, if_icmpge, if_icmpgt, if_icmple
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_acmp_cond">
 * if_acmpeq, if_acmpne
 * </a><br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto">
 * goto
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.jsr">
 * jsr
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifnull">
 * ifnull
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifnonnull">
 * ifnonnull
 * </a>.
 * @author inagaki
 */
public class BranchOpcode extends OpcodeInfo {
    /**
     * branch bytes. (value of jump point).
     */
    public final int branch;

    BranchOpcode(int branch, MnemonicTable opcodeType, int pc) {
        super(opcodeType, pc);
        this.branch = branch;
    }

    /**
     * returns whether this opcode is if_xx.
     * @return in case of this opcode is if_xx, it returns true.
     * otherwise, it returns false.
     */
    public boolean isIf() {
        return  (opcodeType != _goto) && (opcodeType != goto_w) &&
                (opcodeType !=   jsr) && (opcodeType !=  jsr_w);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BranchOpcode)) {
            return false;
        }
        BranchOpcode opcode = (BranchOpcode)obj;
        return super.equals(obj) && (branch == opcode.branch);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + (branch + pc);
    }
}