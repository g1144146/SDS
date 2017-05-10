package sds.classfile.attributes.annotation;

/**
 * This class is for type_parameter_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class TypeParameterTarget extends AbstractTargetInfo {
    private int typeParameterIndex;

    /**
     * constructor.
     * @param typeParameterIndex index of type parameter declaration is annotated
     */
    public TypeParameterTarget(int typeParameterIndex) {
        super(TargetInfoType.TypeParameterTarget);
        this.typeParameterIndex = typeParameterIndex;
    }

    /**
     * returns index of type parameter declaration is annotated.
     * @return index of type parameter declaration
     */
    public int getIndex() {
        return typeParameterIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + typeParameterIndex;
    }
}