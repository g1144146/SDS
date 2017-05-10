package sds.classfile.attributes.annotation;

/**
 * This class is for offset_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class TypeArgumentTarget extends AbstractTargetInfo {
    private int offset;
    private int typeArgumentIndex;

    TypeArgumentTarget(int offset, int index) {
        super(TargetInfoType.TypeArgumentTarget);
        this.offset = offset;
        this.typeArgumentIndex = index;
    }

    /**
     * returns offset.<br>
     * code array offset of either
     * the bytecode instruction corresponding to the cast expression,
     * the new bytecode instruction corresponding to the new expression,
     * the bytecode instruction corresponding to the explicit constructor invocation statement,
     * the bytecode instruction corresponding to the method invocation expression,
     * or the bytecode instruction corresponding to the method reference expression.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * returns index of type in the cast operator is annotated.
     * @return index of type in the cast operator is annotated
     */
    public int getIndex() {
        return typeArgumentIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + typeArgumentIndex + ", offset: " + offset;
    }
}