package sds.classfile.bytecode;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.newarray">
 * newarray
 * </a>.
 * @author inagaki
 */
public class NewArray extends OpcodeInfo {
    private String atype;

    NewArray(int type, int pc) {
        super(MnemonicTable.newarray, pc);
        switch(type) {
            case 4:  this.atype = "boolean"; break;
            case 5:  this.atype = "char";    break;
            case 6:  this.atype = "float";   break;
            case 7:  this.atype = "double";  break;
            case 8:  this.atype = "byte";    break;
            case 9:  this.atype = "short";   break;
            case 10: this.atype = "int";     break;
            case 11: this.atype = "long";    break;
            default: throw new RuntimeException("[unknown type]: " + type);
        }
    }

    /**
     * returns array type.
     * @return array type
     */
    public String getAType() {
        return atype;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof NewArray)) {
            return false;
        }
        NewArray opcode = (NewArray)obj;
        return super.equals(obj) && atype.equals(opcode.atype);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + atype;
    }
}