package sds.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.Code;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.LineNumberTable;
import sds.classfile.attributes.LocalVariable;
import sds.classfile.attributes.annotation.ParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeAnnotations;
import sds.classfile.attributes.annotation.RuntimeParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeTypeAnnotations;
import sds.classfile.attributes.annotation.TargetInfo;
import sds.classfile.attributes.annotation.TypePath;
import sds.classfile.attributes.annotation.LocalVarTarget;
import sds.classfile.attributes.annotation.TypeAnnotation;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.constantpool.ConstantInfo;

import static sds.classfile.attributes.annotation.AnnotationParser.parseAnnotation;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 * This class is for debugging classfile.
 * @author inagaki
 */
public class ClassFilePrinter extends Printer {
    private ConstantInfo[] pool;
    private String sep = System.getProperty("line.separator");
    private OpcodeInfo[] opcodes;

    /**
     * constructor.
     * @param pool constant-pool
     */
    public ClassFilePrinter(ConstantInfo[] pool) {
        this.pool = pool;
    }

    public ClassFilePrinter() {}

    /**
     * prints major and minor version.
     * @param magicNum magic number
     * @param majorVersion major version
     * @param minorVersion minor version
     */
    public void printNumber(int magicNum, int majorVersion, int minorVersion) {
        println("*** Magic Number  *** " + sep + Integer.toHexString(magicNum) + sep);
        println("*** Major Version *** " + sep + majorVersion + sep);
        println("*** Minor Version *** " + sep + minorVersion + sep);
    }

    /**
     * prints constant-pool.
     */
    public void printConstantPool() {
        if(hasElements(pool.length, "ConstantPool", true)) {
            for(int i = 0; i < pool.length; i++) {
                println("  [" + (i+1) + "]: " + pool[i]);
            }
        }
        print(sep);
    }

    /**
     * prints access flag of this class.
     * @param accessFlag
     */
    public void printAccessFlag(int accessFlag) {
        println("*** Access Flag *** ");
        println(AccessFlags.get(accessFlag, "class"));
        print(sep);
    }

    /**
     * prints this class.
     * @param thisClass
     */
    public void printThisClass(int thisClass) {
        println("*** This Class *** ");
        if(checkRange(thisClass)) {
            println(extract(thisClass, pool));
        }
        print(sep);
    }

    /**
     * prints super class.
     * @param superClass
     */
    public void printSuperClass(int superClass) {
        println("*** Super Class *** ");
        if(checkRange(superClass)) {
            println(extract(superClass, pool));
            print(sep);
            return;
        }
        println("has no super class.");
    }

    /**
     * prints interfaces.
     * @param interfaces
     */
    public void printInterface(int[] interfaces) {
        if(hasElements(interfaces.length, "Interfaces", true)) {
            for(int i : interfaces) {
                println(extract(i, pool));
            }
            print(sep);
        }
    }

    public void printMembers(MemberInfo[] m, String type) throws Exception {
        if(hasElements(m.length, type, true)) {
            int i = 0;
            for(MemberInfo f : m) {
                println(i + 1 + ". " + f.toString());
                for(AttributeInfo a : f.getAttr()) {
                    printAttributeInfo(a, "  ");
                }
                print(sep);
                i++;
            }
        }
    }

    /**
     * prints attributes of this class.
     * @param attr
     * @throws IOException
     */
    public void printAttributes(AttributeInfo[] attr) throws Exception {
        if(hasElements(attr.length, "Attributes", false)) {
            for(AttributeInfo a : attr) {
                printAttributeInfo(a, "  ");
            }
            println(sep);
        }
    }

    private boolean hasElements(int length, String type, boolean nextLine) {
        println("*** " + type + " ***");
        if(length == 0) {
            if(nextLine) println("has no " + type + "." + sep);
            else         print("has no "   + type + "." + sep);
            return false;
        }
        return true;
    }

    /**
     * print an attribute.
     * @param info
     * @throws Exception
     */
    public void printAttributeInfo(AttributeInfo info, String indent) throws Exception {
        switch(info.getClass().getSimpleName()) {
            case "BootstrapMethods":
                println(indent + info);
                BootstrapMethods bsm = (BootstrapMethods)info;
                String[] bsmRef = bsm.bsmRef;
                String[][] bsmArg = bsm.bootstrapArgs;
                for(int i = 0; i < bsmRef.length; i++) {
                    println(indent + indent + "bsm ref: " + bsmRef[i]);
                    for(String arg : bsmArg[i]) {
                        println(indent + indent + "bsm args: " + arg);
                    }
                }
                break;
            case "Code":
                println(indent + info);
                Code code = (Code)info;
                println(indent + indent + "max_stack: " + code.maxStack + ", max_locals: " + code.maxLocals);
                this.opcodes = code.opcodes;
                for(OpcodeInfo op : opcodes) {
                    println(indent + indent + op.getPc() + " - " + op);
                }
                if(code.exceptionTable.length > 0) {
                    println("  ExceptionTable");
                    int[][] ex = code.exceptionTable;
                    String[] cTable = code.catchTable;
                    for(int i = 0; i < cTable.length; i++) {
                        print(indent + indent + (i + 1) + ". pc: " + ex[i][0] + "-" + ex[i][1]
                              + ", handler: " + ex[i][2]);
                        println(", catch_type: " + cTable[i]);
                    }
                }
                for(AttributeInfo a : code.attr) {
                    printAttributeInfo(a, indent + indent);
                }
                break;
            case "InnerClasses":
                println(indent + info);
                InnerClasses ic = (InnerClasses)info;
                int classIndex = 1;
                for(String[] c : ic.classes) {
                    println(indent + indent + classIndex + ". " + c[3] + c[0]);
                    classIndex++;
                }
                break;
            case "LineNumberTable":
                println(indent + info);
                LineNumberTable lnt = (LineNumberTable)info;
                int lineIndex = 1;
                for(int[] t : lnt.table) {
                    print(indent + indent + lineIndex + ". start_pc: " + t[0] + ", end_pc: " + t[1]);
                    println(", line_number: " + t[2]);
                    lineIndex++;
                }
                break;
            case "LocalVariable":
                LocalVariable lv = (LocalVariable)info;
                println(indent + lv.typeName);
                int[][] lvt = lv.table;
                String[] name = lv.name;
                String[] desc = lv.desc;
                for(int i = 0; i < name.length; i++) {
                    print(indent + indent + (i+1) + ". pc: " + lvt[i][0] + "-" + (lvt[i][0] + lvt[i][1] - 1));
                    println(", name: " + name[i] + ", index: " + lvt[i][2] + ", desc: " + desc[i]);
                }
                break;
            case "RuntimeAnnotations":
                RuntimeAnnotations rva = (RuntimeAnnotations)info;
                println(indent + rva.name);
                int rvaIndex = 1;
                for(String a : rva.getAnnotations()) {
                    println(indent + indent + rvaIndex + "." + a);
                    rvaIndex++;
                }
                break;
            case "RuntimeParameterAnnotations":
                RuntimeParameterAnnotations rvpa = (RuntimeParameterAnnotations)info;
                println(indent + rvpa.name);
                int rvpaIndex = 1;
                for(String[] pa : rvpa.parameterAnnotations) {
                    println(indent + indent + rvpaIndex + ".");
                    for(String a : pa) {
                        println(indent + indent + indent + a);
                    }
                    rvpaIndex++;
                }
                break;
            case "RuntimeTypeAnnotations":
                RuntimeTypeAnnotations rvta = (RuntimeTypeAnnotations)info;
                println(indent + rvta.name);
                int rvtaIndex = 1;
                for(TypeAnnotation pa : rvta.types) {
                    println(indent + indent + rvtaIndex + "." + parseAnnotation(pa, new SDSStringBuilder(), pool));
                    printTargetInfo(pa.getTargetInfo(), indent + indent + indent);
                    printTypePath(pa.getTargetPath(), indent + indent + indent);
                    rvtaIndex++;
                }
                break;
            case "StackMapTable":
                println(indent + info);
                StackMapTable smt = (StackMapTable)info;
                Map<Integer, Map<String, List<String>>> stackMap = smt.entries;
                for(Integer i : stackMap.keySet()) {
                    Map<String, List<String>> map = stackMap.get(i.intValue());
                    println(indent + indent + i + ": " + map);
                }
                break;
            default:
                println(indent +  info.toString());
                break;
        }
    }

    private void printTargetInfo(TargetInfo info, String indent) {
        if(info instanceof LocalVarTarget) {
            LocalVarTarget lvt = (LocalVarTarget)info;
            println(indent + info.getType());
            for(int[] t : lvt.getTable()) {
                println(indent + "index: " + t[2] + ", pc: " + t[0] + "-" + (t[0] + t[1] - 1));
            }
            return;
        }
        println(indent + info.getType());
    }

    private void printTypePath(TypePath path, String indent) {
        for(int i = 0; i < path.getArgIndex().length; i++) {
            print(indent + "type_path_type: ");
            int pk = path.getPathKind()[i];
            int ai = path.getArgIndex()[i];
            switch(pk) {
                case 0:
                    println("Annotation is deeper in an array type.");
                    break;
                case 1:
                    println("Annotation is deeper in a nested type.");
                    break;
                case 2:
                    println("Annotation is on the bound of a "
                            + "wildcard type argument of a parameterized type.");
                    break;
                case 3:
                    println("Annotation is on a type argument of a parameterized type.");
                    break;
                default:
                    println("unknown type_path_kind: " + pk);
                    break;
            }
            println("       type_arg_index: " + ai);
        }
    }

    private boolean checkRange(int index) {
        return (0 <= index) && (index < pool.length);
    }
}