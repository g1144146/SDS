package sds.classfile.bytecode;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iinc">
 * iinc
 * </a>.
 * @author inagaki
 */
public class Iinc extends OpcodeInfo {
    private int index;
    private int _const;

    Iinc(int index, int _const, int pc) {
        super(MnemonicTable.iinc, pc);
        this.index = index;
        this._const = _const;
    }

    /**
     * returns index into the local variable array of the current frame.
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * returns const.
     * @return const
     */
    public int getConst() {
        return _const;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Iinc)) {
            return false;
        }
        Iinc opcode = (Iinc)obj;
        boolean flag = true;
        flag &= (index == opcode.index);
        flag &= (_const == opcode._const);
        return super.equals(obj) && flag;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + index + ", " + _const;
    }
}