package sds.classfile.bytecode;

import java.io.IOException;

import static sds.classfile.bytecode.MnemonicTable.ldc;
import static sds.classfile.bytecode.MnemonicTable.ldc_w;
import static sds.classfile.bytecode.MnemonicTable.ldc2_w;
import sds.classfile.constantpool.ConstantInfo;
import static sds.classfile.constantpool.ConstantType.C_CLASS;
import static sds.classfile.constantpool.ConstantType.C_DOUBLE;
import static sds.classfile.constantpool.ConstantType.C_FLOAT;
import static sds.classfile.constantpool.ConstantType.C_INTEGER;
import static sds.classfile.constantpool.ConstantType.C_LONG;
import static sds.classfile.constantpool.ConstantType.C_METHOD_HANDLE;
import static sds.classfile.constantpool.ConstantType.C_STRING;

import static sds.classfile.bytecode.OperandExtractor.extractOperand;

/**
 * This class is for opcode has constant-pool entry index.<br>
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc">
 * ldc
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc_w">
 * ldc_w
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc2_w">
 * ldc2_w
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.getstatic">
 * getstatic
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.putstatic">
 * putstatic
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.getfield">
 * getfield
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.putfield">
 * putfield
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokevirtual">
 * invokevirtual
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokespecial">
 * invokespecial
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokestatic">
 * invokestatic
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.new">
 * new
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.checkcast">
 * checkcast
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.anewarray">
 * anewarray
 * </a>,
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.instanceof">
 * instanceof
 * </a>
 * @author inagaki
 */
public class CpRefOpcode extends OpcodeInfo {
    int index;
    String operand;
    String type = "";

    /**
     * constrcutor.
     * @param index constant-pool entry index
     * @param pool constant-pool
     * @param opcodeType opcode type
     * @param pc index into the code array
     */
    public CpRefOpcode(int index, ConstantInfo[] pool, MnemonicTable opcodeType, int pc) throws IOException {
        super(opcodeType, pc);
        MnemonicTable opType = opcodeType;
        this.index = index;
        this.operand = extractOperand(this, pool);
        if(opType == ldc || opType == ldc_w || opType == ldc2_w) {
            switch(pool[index - 1].getTag()) {
                case C_DOUBLE:  this.type = "double"; break;
                case C_FLOAT:   this.type = "float";  break;
                case C_INTEGER: this.type = "int";    break;
                case C_LONG:    this.type = "long";   break;
                case C_STRING:
                    this.type = "String";
                    operand = "\"" + escape(operand) + "\"";
                    break;
                case C_CLASS:   this.type = extract(index, pool); break;
                case C_METHOD_HANDLE: break;
                default:
                    throw new IllegalStateException("LDC opcode refers unknown constant type info.");
            }
        }
    }

    private String escape(String string) {
        return string.replaceAll("\n", "\\\\n");
    }

    /**
     * returns constant-pool entry index.
     * @return constant-pool entry index
     */
    public int getIndexByte() {
        return index;
    }

    /**
     * returns operand which this opcode has.
     * @return operand.
     */
    public String getOperand() {
        return operand;
    }

    /**
     * returns type.<br>
     * returns 0-length string in case of opcode type is not ldc, ldc_w or ldc2_w.
     * @return type
     */
    public String getLDCType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CpRefOpcode)) {
            return false;
        }
        CpRefOpcode opcode = (CpRefOpcode)obj;
        boolean flag = true;
        flag &= (index == opcode.index);
        flag &= operand.equals(opcode.operand);
        flag &= type.equals(opcode.type);
        return super.equals(obj) && flag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(": #").append(index).append("(").append(operand);
        if(type.length() > 0) {
            sb.append("(").append(type).append(")");
        }
        return sb.append(")").toString();
    }
}