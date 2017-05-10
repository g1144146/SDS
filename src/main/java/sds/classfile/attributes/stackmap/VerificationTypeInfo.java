package sds.classfile.attributes.stackmap;

/**
 * This class is for
 * Top_variable_info, Null_variable_info, UninitializedThis_variable_info
 * Integer_variable_info, Float_variable_info,
 * Long_variable_info and Double_variable_info
 * which {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
class VerificationTypeInfo {
    private String type;

    VerificationTypeInfo(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + "TypeInfo";
    }
}

/**
 * This class is for Object_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
class ObjectVariableInfo extends VerificationTypeInfo {
    private int cpoolIndex;

    ObjectVariableInfo(int cpoolIndex) {
        super("object");
        this.cpoolIndex = cpoolIndex;
    }

    /**
     * returns constant-pool entry index.
     * @return constant-pool entry index
     */
    public int getCPoolIndex() {
        return cpoolIndex;
    }
}

/**
 * This class is for Uninitialized_variable_info which
 * {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
class UninitializedVariableInfo extends VerificationTypeInfo {
    private int offset;

    UninitializedVariableInfo(int offset) {
        super("variable");
        this.offset = offset;
    }

    /**
     * returns offset.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }
}