package sds.classfile.attributes.annotation;

/**
 * This class is for offset_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class OffsetTarget extends AbstractTargetInfo {
    private int offset;

    OffsetTarget(int offset) {
        super(TargetInfoType.OffsetTarget);
        this.offset = offset;
    }

    /**
     * returns offset.<br>
     * code array offset of either
     * the instanceof bytecode instruction corresponding to the instanceof expression,
     * the new bytecode instruction corresponding to the new expression,
     * or the bytecode instruction corresponding to the method reference expression.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "offset: " + offset;
    }
}