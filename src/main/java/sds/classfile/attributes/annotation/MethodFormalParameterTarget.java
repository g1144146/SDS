package sds.classfile.attributes.annotation;

/**
 * This class is for formal_parameter_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class MethodFormalParameterTarget extends AbstractTargetInfo {
    private int formalParameterIndex;

    MethodFormalParameterTarget(int formalParameterIndex) {
        super(TargetInfoType.MethodFormalParameterTarget);
        this.formalParameterIndex = formalParameterIndex;
    }

    /**
     * returns index of formal parameter declaration has an annotated type.
     * @return index of formal parameter declaration
     */
    public int getIndex() {
        return formalParameterIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + formalParameterIndex;
    }
}