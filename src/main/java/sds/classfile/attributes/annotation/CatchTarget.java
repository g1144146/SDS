package sds.classfile.attributes.annotation;

/**
 * This class is for catch_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class CatchTarget extends AbstractTargetInfo {
    private int exceptionTableIndex;

    CatchTarget(int index) {
        super(TargetInfoType.CatchTarget);
        this.exceptionTableIndex = index;
    }

    /**
     * returns index into the exception_table array.<br>
     * the index into the exception_table array of the
     * {@link sds.classfile.attributes.Code
     * <code>Code attribute</code>}
     * enclosing the
     * {@link RuntimeVisibleTypeAnnotations 
     * <code>RuntimeVisibleTypeAnnotations attribute</code>}.
     * @return index into the exception_table array
     */
    public int getIndex() {
        return exceptionTableIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + exceptionTableIndex;
    }
}