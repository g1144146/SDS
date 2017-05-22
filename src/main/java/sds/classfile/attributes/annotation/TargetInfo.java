package sds.classfile.attributes.annotation;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a>.<br>
 * {@link TypeAnnotation <code>TypeAnnotation</code>} has union.
 * @author inagaki
 */
public abstract class TargetInfo {
    public final TargetInfoType type;

    TargetInfo(TargetInfoType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}

class CatchTarget extends TargetInfo {
    public final int exceptionTableIndex;

    CatchTarget(int index) {
        super(TargetInfoType.CatchTarget);
        this.exceptionTableIndex = index;
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
    public final int formalParameterIndex;

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
    public final int offset;

    OffsetTarget(int offset) {
        super(TargetInfoType.OffsetTarget);
        this.offset = offset;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "offset: " + offset;
    }
}

class SuperTypeTarget extends TargetInfo {
    public final int superTypeIndex;

    SuperTypeTarget(int superTypeIndex) {
        super(TargetInfoType.SuperTypeTarget);
        this.superTypeIndex = superTypeIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + superTypeIndex;
    }
}

class ThrowsTarget extends TargetInfo {
    public final int throwsTypeIndex;

    ThrowsTarget(int throwsTypeIndex) {
        super(TargetInfoType.ThrowsTarget);
        this.throwsTypeIndex = throwsTypeIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + throwsTypeIndex;
    }
}

class TypeArgumentTarget extends TargetInfo {
    public final int offset;
    public final int typeArgumentIndex;

    TypeArgumentTarget(int offset, int index) {
        super(TargetInfoType.TypeArgumentTarget);
        this.offset = offset;
        this.typeArgumentIndex = index;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + typeArgumentIndex + ", offset: " + offset;
    }
}

class TypeParameterBoundTarget extends TargetInfo {
    public final int typeParameterIndex;
    public final int boundIndex;

    TypeParameterBoundTarget(int typeParameterIndex, int boundIndex) {
        super(TargetInfoType.TypeParameterBoundTarget);
        this.typeParameterIndex = typeParameterIndex;
        this.boundIndex = boundIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "bound_index: " + boundIndex + ", type_parameter_index: " + typeParameterIndex;
    }
}

class TypeParameterTarget extends TargetInfo {
    public final int typeParameterIndex;

    TypeParameterTarget(int typeParameterIndex) {
        super(TargetInfoType.TypeParameterTarget);
        this.typeParameterIndex = typeParameterIndex;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + "index: " + typeParameterIndex;
    }
}