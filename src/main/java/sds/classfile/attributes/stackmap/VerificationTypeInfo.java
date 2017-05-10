package sds.classfile.attributes.stackmap;

/**
 * This class is for
 * Top_variable_info, Null_variable_info, UninitializedThis_variable_info
 * Integer_variable_info, Float_variable_info,
 * Long_variable_info and Double_variable_info
 * which {@link VerificationTypeInfo <code>VerificationTypeInfo</code>}.
 * @author inagaki
 */
public class VerificationTypeInfo {
    private String type;

    VerificationTypeInfo(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + "TypeInfo";
    }
}

class ObjectVariableInfo extends VerificationTypeInfo {
    private int cpoolIndex;

    ObjectVariableInfo(int cpoolIndex) {
        super("object");
        this.cpoolIndex = cpoolIndex;
    }

    public int getCPoolIndex() {
        return cpoolIndex;
    }
}

class UninitializedVariableInfo extends VerificationTypeInfo {
    private int offset;

    UninitializedVariableInfo(int offset) {
        super("variable");
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}