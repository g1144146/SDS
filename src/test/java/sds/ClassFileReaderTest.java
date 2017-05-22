package sds;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import sds.classfile.*;
import sds.classfile.attributes.*;
import sds.classfile.attributes.annotation.*;
import sds.classfile.bytecode.*;
import sds.classfile.constantpool.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static sds.util.AccessFlags.get;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

public class ClassFileReaderTest {
    private ClassFile cf;
    private ClassFile cfAnnotation;
    
    @Before
    public void setUp() {
        String filePath = generateFilePath("build" , "resources", "test", "resources", "Hello.class");
        ClassFileReader reader = new ClassFileReader(filePath); 
        reader.read();
        this.cf = reader.getClassFile();

        String path = generateFilePath("build", "classes", "test", "sds", "AnnotatedTest.class");
        ClassFileReader r = new ClassFileReader(path); 
        r.read();
        this.cfAnnotation = r.getClassFile();
    }
    
    private String generateFilePath(String... paths) {
        StringBuilder sb = new StringBuilder(paths.length * 2);
        String sep = File.separator;
        for(int i = 0; i < paths.length - 1; i++) {
            sb.append(paths[i]).append(sep);
        }
        sb.append(paths[paths.length-1]);
        return sb.toString();
    }

    @Test
    public void testAssembling() throws Exception {
        assertThat(Integer.toHexString(cf.magicNumber), is("cafebabe"));
        assertThat(cf.majorVersion, is(52));
        assertThat(cf.minorVersion, is(0));
        assertThat(get(cf.accessFlag, "class"), is("public class "));
        assertThat(cf.fields, is(new MemberInfo[0]));
        assertThat(cf.interfaces, is(new int[0]));
        String[] constantPool = {
            "CONSTANT_METHODREF", "CONSTANT_FIELDREF", "CONSTANT_STRING", "CONSTANT_METHODREF", "CONSTANT_CLASS",
            "CONSTANT_CLASS", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8",
            "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_CLASS", "CONSTANT_UTF8", "CONSTANT_UTF8",
            "CONSTANT_NAME_AND_TYPE", "CONSTANT_CLASS", "CONSTANT_NAME_AND_TYPE","CONSTANT_UTF8", "CONSTANT_CLASS",
            "CONSTANT_NAME_AND_TYPE", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8",
            "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8", "CONSTANT_UTF8"
        };
        int index = 0;
        ConstantInfo[] pool = cf.pool;
        for(int i = 0; i < pool.length; i++) {
            assert pool[i].toString().startsWith(constantPool[index++]);
        }
        assertThat(extract(cf.superClass, pool), is("Object"));
        assertThat(extract(cf.thisClass, pool), is("Hello"));
        String source = ((SourceFile)cf.attr[0]).sourceFile;
        assertThat(source, is("Hello.java"));
    }

    @Test
    public void testAssembling2() {
        ConstantInfo[] pool = cfAnnotation.pool;
        MemberInfo[] f = cfAnnotation.fields;
        MemberInfo m = f[0];
        {
            assertThat(m.getAccessFlags(), is("private "));
            String descAndName = m.getDescriptor() + " " + m.getName();
            assertThat(descAndName, is("String field"));
            
            AttributeInfo[] itr = m.getAttr();
            RuntimeAnnotations a1 = (RuntimeAnnotations)itr[0];
            assertThat(a1.annotations[0], is("@sds.RuntimeAnnotation(value = \"field\")"));
            
            RuntimeTypeAnnotations a2 = (RuntimeTypeAnnotations)itr[1];
            assertThat(a2.types[0].getTargetInfo().getType().toString(), is("EmptyTarget"));
            assertThat(a2.annotations[0], is("@sds.RuntimeAnnotation(value = \"field\")"));
        }
        
        // public void method(int)    
        MemberInfo[] mItr = cfAnnotation.methods;
        MemberInfo method = mItr[1];
        {
            assertThat(method.getAccessFlags(), is("public "));
            assertThat(method.getDescriptor() + " " + method.getName(), is("(int)void method"));
            // code attribute in method.
            AttributeInfo[] itr = method.getAttr();
            Code code = (Code)itr[0];
            { // attributes in code attribute and opcodes.
                assertThat(code.maxStack, is(1));
                assertThat(code.maxLocals, is(3));
                OpcodeInfo[] op = code.opcodes; // opcodes
                assertThat(op[0].getType(), is(MnemonicTable.aload_0));
                assertThat(op[1].getType(), is(MnemonicTable.inovokedynamic));
                assertThat(op[2].getType(), is(MnemonicTable.astore_2));
                assertThat(op[3].getType(), is(MnemonicTable._return));
                
                AttributeInfo[] codeItr = code.attr;
                
                int[][] table = ((LineNumberTable)codeItr[0]).table;
                assertThat(table[0][1], is(7));
                assertThat(table[0][2], is(27));
                
                int[][] table2 = ((LocalVariable)codeItr[1]).table;
                String[] name2 = ((LocalVariable)codeItr[1]).name;
                String[] desc2 = ((LocalVariable)codeItr[1]).desc;
                assertThat(table2[0][0], is(0));
                assertThat(table2[0][1], is(8));
                assertThat(table2[0][2], is(0));
                assertThat(desc2[0], is("sds.AnnotatedTest"));
                assertThat(name2[0], is("this"));
                
                int[][] table3 = ((LocalVariable)codeItr[2]).table;
                String[] name3 = ((LocalVariable)codeItr[2]).name;
                String[] desc3 = ((LocalVariable)codeItr[2]).desc;
                assertThat(table3[0][0], is(7));
                assertThat(table3[0][1], is(1));
                assertThat(table3[0][2], is(2));
                assertThat(desc3[0], is("java.util.function.Consumer<T>"));
                assertThat(name3[0], is("c"));

                // runtime visible type annotations , and items of that.
                RuntimeTypeAnnotations r = (RuntimeTypeAnnotations)codeItr[3];
                TypeAnnotation ta = r.types[0];
                assertThat(ta.getTargetInfo().getType(), is(TargetInfoType.LocalVarTarget));
                TypePath tp = ta.getTargetPath();
                assertThat(tp.getArgIndex()[0], is(0));
                ElementValuePair evp = ta.getElementValuePairs()[0];
                assertThat(extract(evp.getElementNameIndex(), pool), is("value"));
                assertThat(extract(evp.getValue().getConstValueIndex(), pool), is("generics_type"));
            }
            Exceptions e = (Exceptions)itr[1];
            assertThat(e.exceptionTable[0], is("Exception"));
            sds.classfile.attributes.Deprecated dep = (sds.classfile.attributes.Deprecated)itr[2];
            assertThat(dep.toString(), is("[Deprecated]"));
            
            Signature sig = (Signature)itr[3];
            assertThat(sig.signature, is("<T:Ljava/lang/Object;>(I)V"));
            
            // runtime visible annotations , and items of that.
            RuntimeAnnotations rva = (RuntimeAnnotations)itr[4];
            String depAnn = rva.annotations[0];
            assertThat(depAnn, is("@Deprecated"));
            String rep = rva.annotations[1];
            assertThat(rep, is("@sds.RepeatableRuntimeAnnotation(value = {@sds.RuntimeAnnotation(value = \"method\")"
                                                            + ",@sds.RuntimeAnnotation(value = \"return_type\")})"));
            
            // runtime visible type annotations , and items of that.
            RuntimeTypeAnnotations rvta = (RuntimeTypeAnnotations)itr[5];
            assertThat(rvta.types[0].getTargetInfo().getType(), is(TargetInfoType.TypeParameterTarget));
            assertThat(rvta.types[1].getTargetInfo().getType(), is(TargetInfoType.TypeParameterBoundTarget));
            assertThat(rvta.types[2].getTargetInfo().getType(), is(TargetInfoType.ThrowsTarget));
            assertThat(rvta.types[3].getTargetInfo().getType(), is(TargetInfoType.MethodFormalParameterTarget));
            assertThat(rvta.annotations[0], is("@sds.RuntimeAnnotation(value = \"generics_type_definition\")"));
            assertThat(rvta.annotations[1], is("@sds.RuntimeAnnotation(value = \"type_param_extends\")"));
            assertThat(rvta.annotations[2], is("@sds.RuntimeAnnotation(value = \"throws_exception\")"));
            assertThat(rvta.annotations[3], is("@sds.RuntimeAnnotation(value = \"method_arg\")"));
            
            // runtime visible parameter annotations , and items of that.
            RuntimeParameterAnnotations ra = (RuntimeParameterAnnotations)itr[6];
            assertThat(ra.parameterAnnotations[0][0], is("@sds.RuntimeAnnotation(value = \"method_arg\")"));
        }
        
        // attributes which class has.
        AttributeInfo[] cfAnItr = cfAnnotation.attr;
        
        SourceFile sf = (SourceFile)cfAnItr[0];
        assertThat(sf.sourceFile, is("AnnotatedTest.java"));
        
        String[][] classes = ((InnerClasses)cfAnItr[2]).classes;
        assertThat(classes[0][3], is("public class "));
        assertThat(classes[0][0], is("sds.AnnotatedTest$Inner"));
        assertThat(classes[1][3], is("public static final enum "));
        assertThat(classes[1][0], is("sds.AnnotatedTest$Type"));
        assertThat(classes[2][3], is("public static final class "));
        assertThat(classes[2][0], is("java.lang.invoke.MethodHandles$Lookup"));

        String[][] bsm = ((BootstrapMethods)cfAnItr[3]).bootstrapArgs;
        String[] bsmRef = ((BootstrapMethods)cfAnItr[3]).bsmRef;
        assertThat(bsmRef[0], is("java.lang.invoke.LambdaMetafactory.metafactory|("
                        + "java.lang.invoke.MethodHandles$Lookup,String,java.lang.invoke.MethodType,"
                        + "java.lang.invoke.MethodType,java.lang.invoke.MethodHandle,java.lang.invoke.MethodType)"
                        + "java.lang.invoke.CallSite"));
        assertThat(bsm[0][0], is("(Object)void"));
        assertThat(bsm[0][1], is("sds.AnnotatedTest.lambda$method$0|(Object)void"));
    }
}
