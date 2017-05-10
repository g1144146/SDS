package sds.classfile.attributes.annotation;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a>.<br>
 * {@link TypeAnnotation <code>TypeAnnotation</code>} has union.
 * @author inagaki
 */
public abstract class TargetInfo {
    private TargetInfoType type;

    TargetInfo(TargetInfoType type) {
        this.type = type;
    }

    /**
     * retunrs target info type
     * @return target info type
     */
    public TargetInfoType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}

class CatchTarget extends TargetInfo {
    private int exceptionTableIndex;

    CatchTarget(int index) {
        super(TargetInfoType.CatchTarget);
        this.exceptionTableIndex = index;
    }

    public int getIndex() {
        return exceptionTableIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + exceptionTableIndex;
    }
}

class EmptyTarget extends TargetInfo {
    EmptyTarget() {
        super(TargetInfoType.EmptyTarget);
    }
}

class MethodFormalParameterTarget extends TargetInfo {
    private int formalParameterIndex;

    MethodFormalParameterTarget(int formalParameterIndex) {
        super(TargetInfoType.MethodFormalParameterTarget);
        this.formalParameterIndex = formalParameterIndex;
    }

    public int getIndex() {
        return formalParameterIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + formalParameterIndex;
    }
}

class OffsetTarget extends TargetInfo {
    private int offset;

    OffsetTarget(int offset) {
        super(TargetInfoType.OffsetTarget);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "offset: " + offset;
    }
}

class SuperTypeTarget extends TargetInfo {
    private int superTypeIndex;

    SuperTypeTarget(int superTypeIndex) {
        super(TargetInfoType.SuperTypeTarget);
        this.superTypeIndex = superTypeIndex;
    }

    public int getIndex() {
        return superTypeIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + superTypeIndex;
    }
}

class ThrowsTarget extends TargetInfo {
    private int throwsTypeIndex;

    ThrowsTarget(int throwsTypeIndex) {
        super(TargetInfoType.ThrowsTarget);
        this.throwsTypeIndex = throwsTypeIndex;
    }

    public int getIndex() {
        return throwsTypeIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + throwsTypeIndex;
    }
}

class TypeArgumentTarget extends TargetInfo {
    private int offset;
    private int typeArgumentIndex;

    TypeArgumentTarget(int offset, int index) {
        super(TargetInfoType.TypeArgumentTarget);
        this.offset = offset;
        this.typeArgumentIndex = index;
    }

    public int getOffset() {
        return offset;
    }

    public int getIndex() {
        return typeArgumentIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + typeArgumentIndex + ", offset: " + offset;
    }
}

class TypeParameterBoundTarget extends TargetInfo {
    private int typeParameterIndex;
    private int boundIndex;

    TypeParameterBoundTarget(int typeParameterIndex, int boundIndex) {
        super(TargetInfoType.TypeParameterBoundTarget);
        this.typeParameterIndex = typeParameterIndex;
        this.boundIndex = boundIndex;
    }

    public int getTPI() {
        return typeParameterIndex;
    }

    public int getBoundIndex() {
        return boundIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "bound_index: " + boundIndex + ", type_parameter_index: " + typeParameterIndex;
    }
}

class TypeParameterTarget extends TargetInfo {
    private int typeParameterIndex;

    TypeParameterTarget(int typeParameterIndex) {
        super(TargetInfoType.TypeParameterTarget);
        this.typeParameterIndex = typeParameterIndex;
    }

    public int getIndex() {
        return typeParameterIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + typeParameterIndex;
    }
}