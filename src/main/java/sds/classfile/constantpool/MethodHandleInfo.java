package sds.classfile.constantpool;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.8">
 * Constant_MethodHandle_Info</a>.
 * @author inagaki
 */
public class MethodHandleInfo extends ConstantInfo {
    private int refKind;
    private int refIndex;
    
    MethodHandleInfo(int refKind, int refIndex) {
        super(ConstantType.C_METHOD_HANDLE);
        this.refKind  = refKind;
        this.refIndex = refIndex;
    }

    /**
     * returns kind of this method handle.
     * @return kind of this method handle
     */
    public int getRefKind() {
        return refKind;
    }

    /**
     * returns constant-pool entry index of constant info.
     * @return constant-pool entry index of constant info
     */
    public int getRefIndex() {
        return refIndex;
    }

    /**
     * returns desctiption of reference kind of this method handle.
     * @return desctiption
     */
    public String getRefKindValue() {
        switch(refKind) {
            case 1:  return "REF_getField";
            case 2:  return "REF_getStatic";
            case 3:  return "REF_putField";
            case 4:  return "REF_putStatic";
            case 5:  return "REF_invokeVirtual";
            case 6:  return "REF_invokeStatic";
            case 7:  return "REF_invokeSpecial";
            case 8:  return "REF_newInvokeSpecial";
            case 9:  return "REF_invokeInterface";
            default: throw new RuntimeException("unknown reference kind.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\t" + getRefKindValue() + ":#" + refIndex;
    }
}