package sds.assemble;

import sds.ClassFile;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.BootstrapMethods;
import sds.classfile.attributes.EnclosingMethod;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.SourceFile;
import sds.classfile.attributes.annotation.RuntimeTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeTypeAnnotations;
import sds.classfile.attributes.annotation.SuperTypeTarget;
import sds.classfile.attributes.annotation.TargetInfo;
import sds.classfile.constantpool.ConstantInfo;
import static sds.util.AccessFlags.get;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 * This class is for contents of class.
 * @author inagaki
 */
public class ClassContent extends BaseContent {
    MethodContent[] methods;
    FieldContent[] fields;
    NestedClass[] nested;
    String sourceFile;
    String[] bootstrapMethods;
    String accessFlag;
    String thisClass;
    String superClass;
    String[] interfaces;
    boolean isEnclosed;
    String enclosingClass;
    String enclosingMethod;

    /**
     * constructor.
     * @param cf classfile
     */
    public ClassContent(ClassFile cf) {
        ConstantInfo[] pool = cf.getPool();
        this.accessFlag = get(cf.getAccessFlag(), "class");
        this.thisClass  = extract(cf.getThisClass(),  pool);
        this.superClass = extract(cf.getSuperClass(), pool);
        if(cf.getInterfaces().length > 0) {
            int[] interIndex = cf.getInterfaces();
            this.interfaces = new String[interIndex.length];
            for(int i = 0; i < interfaces.length; i++) {
                interfaces[i] = extract(interIndex[i], pool);
            }
        }
        int arrayIndex = 0;
        MemberInfo[] method = cf.getMethods();
        this.methods = new MethodContent[method.length];
        for(MemberInfo m : method) {
            methods[arrayIndex++] = new MethodContent(m, pool);
        }

        arrayIndex = 0;
        MemberInfo[] field = cf.getFields();
        this.fields = new FieldContent[field.length];
        for(MemberInfo f : field) {
            fields[arrayIndex++] = new FieldContent(f, pool);
        }

        InnerClasses ic = null;
        for(AttributeInfo info : cf.getAttr()) {
            if(info instanceof InnerClasses) {
                ic = (InnerClasses)info;
            }
            analyzeAttribute(info, pool);
        }
        if(ic != null) {
            this.nested = new NestedClass[ic.getClasses().length];
            String[][] c = ic.getClasses();
            for(int i = 0; i < c.length; i++) {
                nested[i] = new NestedClass(c[i]);
            }
        }
    }

    public ClassContent() {}

    @Override
    public void analyzeAttribute(AttributeInfo info, ConstantInfo[] pool) {
        switch(info.getType()) {
            case BootstrapMethods:
                BootstrapMethods bsm = (BootstrapMethods)info;
                this.bootstrapMethods = new String[bsm.getBSMArgs().length];
                int index = 0;
                for(String[] b : bsm.getBSMArgs()) {
                    for(String arg : b) {
                        if(! arg.startsWith("(")) {
                            bootstrapMethods[index++] = arg;
                        }
                    }
                }
                break;
            case EnclosingMethod:
                EnclosingMethod em = (EnclosingMethod)info;
                this.enclosingClass  = em.getEncClass();
                this.enclosingMethod = em.getEncMethod();
                break;
            case InnerClasses:
                InnerClasses ic = (InnerClasses)info;
//                for(InnerClasses.Classes c : ic.getClasses()) {
//                    int inner = c.getNumber("inner");
//                    int outer = c.getNumber("outer");
//                    int name = c.getNumber("inner_name");
//                    int accessFlag = c.getNumber("access_flag");
//                }
                break;
            case RuntimeVisibleTypeAnnotations:
                RuntimeTypeAnnotations rvta = (RuntimeTypeAnnotations)info;
                this.taContent = new ClassTypeAnnotationContent(rvta.getAnnotations(), true);
                System.out.println("<<<Runtime Visible Type Annotation>>>: ");
                for(int i = 0; i < taContent.count; i++) {
                    System.out.print(taContent.visible[i]);
                    System.out.println(", " + taContent.targets[i]);
                }
                break;
            case RuntimeInvisibleTypeAnnotations:
                RuntimeTypeAnnotations rita = (RuntimeTypeAnnotations)info;
                if(taContent == null) {
                    this.taContent = new ClassTypeAnnotationContent(rita.getAnnotations(), false);
                } else {
                    taContent.setInvisible(rita.getAnnotations());
                }
                System.out.println("<<<Runtime Invisible Type Annotation>>>: ");
                for(int i = 0; i < taContent.count; i++) {
                    System.out.print(taContent.invisible[i]);
                    System.out.println(", " + taContent.invTargets[i]);
                }
                break;
            case SourceDebugExtension:
                // todo
                break;
            case SourceFile:
                SourceFile sf = (SourceFile)info;
                this.sourceFile = sf.getSourceFile();
                break;
            default:
                super.analyzeAttribute(info, pool);
                break;
        }
    }

    /**
     * returns assembled methods which this class has.
     * @return assembled methods
     */
    public MethodContent[] getMethods() {
        return methods != null ? methods : new MethodContent[0];
    }

    /**
     * returns assembled fields which this class has.
     * @return assembled fields
     */
    public FieldContent[] getFields() {
        return fields != null ? fields : new FieldContent[0];
    }

    /**
     * returns assembled nested classes which this class has.
     * @return assembled nested classes
     */
    public NestedClass[] getNested() {
        return nested != null ? nested : new NestedClass[0];
    }

    /**
     * return source file name of this class.
     * @return source file name
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * returns access flag of this class.
     * @return access flag
     */
    public String getAccessFlag() {
        return accessFlag;
    }

    /**
     * returns this class.
     * @return 
     */
    public String getThisClass() {
        return thisClass;
    }

    /**
     * returns super class of this class.
     * @return 
     */
    public String getSuperClass() {
        return superClass;
    }

    /**
     * returns interfaces which this class implements.
     * @return 
     */
    public String[] getInterfaces() {
        return interfaces != null ? interfaces : new String[0];
    }

    /**
     * This class is
     * {@link TypeAnnotationContent <code>TypeAnnotationContent</code>}
     * for class.
     */
    public class ClassTypeAnnotationContent extends TypeAnnotationContent {
        ClassTypeAnnotationContent(String[] ta, boolean isVisible) {
            super(ta, isVisible);
        }

        @Override
        void initTarget(TargetInfo target, int annIndex, boolean isVisible) {
            String annotation = isVisible ? visible[annIndex] : invisible[annIndex];
            switch(target.getType()) {
                case SuperTypeTarget:
                    SuperTypeTarget stt = (SuperTypeTarget)target;
                    if(stt.getIndex() == -1) {
                        superClass = annotation + " " + superClass;
                    } else {
                        interfaces[stt.getIndex()] = annotation + " " + interfaces[stt.getIndex()];
                    }
                    break;
                case TypeParameterTarget:      break;
                case TypeParameterBoundTarget: break;
            }
        }
    }
}