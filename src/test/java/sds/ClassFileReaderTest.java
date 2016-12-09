package sds;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.*;
import sds.classfile.attributes.annotation.*;
import sds.classfile.bytecode.*;
import sds.classfile.constantpool.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static sds.util.AccessFlags.get;
import static sds.util.Utf8ValueExtractor.extract;

public class ClassFileReaderTest {
	private ClassFile cf;
	private ClassFile cfAnnotation;
	
	@Before
	public void setUp() {
		String filePath = generateFilePath("build" , "resources"
					, "test", "resources", "Hello.class");
		ClassFileReader reader = new ClassFileReader(filePath); 
		reader.read();
		this.cf = reader.getClassFile();

		String path = generateFilePath("build", "classes", "test"
					, "sds", "AnnotatedTest.class");
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
		assertThat(cf.fields.getAll(), is(new MemberInfo[0]));
		assertThat(cf.interfaces, is(new int[0]));
		String[] constantPool
				= {"CONSTANT_METHODREF","CONSTANT_FIELDREF","CONSTANT_STRING"
				,"CONSTANT_METHODREF","CONSTANT_CLASS","CONSTANT_CLASS","CONSTANT_UTF8"
				,"CONSTANT_UTF8","CONSTANT_UTF8","CONSTANT_UTF8","CONSTANT_UTF8"
				,"CONSTANT_UTF8","CONSTANT_UTF8","CONSTANT_CLASS","CONSTANT_UTF8"
				,"CONSTANT_UTF8","CONSTANT_NAME_AND_TYPE","CONSTANT_CLASS"
				,"CONSTANT_NAME_AND_TYPE","CONSTANT_UTF8","CONSTANT_CLASS"
				,"CONSTANT_NAME_AND_TYPE","CONSTANT_UTF8","CONSTANT_UTF8","CONSTANT_UTF8"
				,"CONSTANT_UTF8","CONSTANT_UTF8","CONSTANT_UTF8","CONSTANT_UTF8"
				,"CONSTANT_UTF8","CONSTANT_UTF8"};
		int index = 0;
		for(ConstantInfo info : cf.pool.getAll()) {
			assert info.toString().startsWith(constantPool[index++]);
		}
		ConstantPool pool = cf.pool;
		assertThat(extract(pool.get(cf.superClass-1), pool), is("java.lang.Object"));
		assertThat(extract(pool.get(cf.thisClass-1), pool), is("Hello"));
		String source = ((SourceFile)cf.attr.getAll()[0]).getSourceFile();
		assertThat(source, is("Hello.java"));
	}

	@Test
	public void testAssembling2() {
		ConstantPool pool = cfAnnotation.pool;
		Fields f = cfAnnotation.fields;
		MemberInfo m = f.get(0);
		{
			assertThat(m.getAccessFlags(), is("private "));
			String descAndName = m.getDescriptor() + " " + m.getName();
			assertThat(descAndName, is("java.lang.String field"));
			
			RuntimeVisibleAnnotations a1 = (RuntimeVisibleAnnotations)m.getAttr().get(0);
			assertThat(a1.getAnnotations()[0], is("@sds.RuntimeAnnotation(value = \"field\")"));
			
			RuntimeVisibleTypeAnnotations a2 = (RuntimeVisibleTypeAnnotations)m.getAttr().get(1);
			assertThat(a2.getTypes()[0].getTargetInfo().getType().toString(), is("EmptyTarget"));
			assertThat(a2.getAnnotations()[0], is("@sds.RuntimeAnnotation(value = \"field\")"));
		}
		
		// public void method(int)	
		MemberInfo method = cfAnnotation.methods.get(1);
		{
			assertThat(method.getAccessFlags(), is("public "));
			assertThat(method.getDescriptor() + " " + method.getName(), is("(int)void method"));
			// code attribute in method.
			Code code = (Code)method.getAttr().get(0);
			{ // attributes in code attribute and opcodes.
				assertThat(code.getMaxStack(), is(1));
				assertThat(code.maxLocals(), is(3));
				OpcodeInfo[] op = code.getCode().getAll(); // opcodes
				assertThat(op[0].getOpcodeType(), is(MnemonicTable.aload_0));
				assertThat(op[1].getOpcodeType(), is(MnemonicTable.inovokedynamic));
				assertThat(op[2].getOpcodeType(), is(MnemonicTable.astore_2));
				assertThat(op[3].getOpcodeType(), is(MnemonicTable._return));
				
				LineNumberTable.LNTable[] table = ((LineNumberTable)code.getAttr().get(0))
						.getLineNumberTable();
				assertThat(table[0].getStartPc(), is(0));
				assertThat(table[0].getLineNumber(), is(27));
				
				LocalVariableTable.LVTable[] table2 = ((LocalVariableTable)code.getAttr().get(1)).getTable();
				assertThat(table2[0].getNumber("start_pc"), is(0));
				assertThat(table2[0].getNumber("length"), is(8));
				assertThat(table2[0].getNumber("index"), is(0));
				assertThat(table2[0].getDesc(), is("sds.AnnotatedTest"));
				assertThat(table2[0].getName(), is("this"));
				
				LocalVariableTypeTable.LVTable[] table3
						= ((LocalVariableTypeTable)code.getAttr().get(2)).getTable();
				assertThat(table3[0].getNumber("start_pc"), is(7));
				assertThat(table3[0].getNumber("length"), is(1));
				assertThat(table3[0].getNumber("index"), is(2));
				assertThat(table3[0].getDesc(), is("java.util.function.Consumer<T>"));
				assertThat(table3[0].getName(), is("c"));

				// runtime visible type annotations , and items of that.
				RuntimeVisibleTypeAnnotations r
						= (RuntimeVisibleTypeAnnotations)code.getAttr().get(3);
				TypeAnnotation ta = r.getTypes()[0];
				assertThat(ta.getTargetInfo().getType(), is(TargetInfoType.LocalVarTarget));
				TypePath tp = ta.getTargetPath();
				assertThat(tp.getArgIndex()[0], is(0));
				ElementValuePair evp = ta.getElementValuePairs()[0];
				assertThat(extract(pool.get(evp.getElementNameIndex()-1), pool), is("value"));
				assertThat(extract(pool.get(evp.getValue().getConstValueIndex()-1), pool)
						, is("generics_type"));
			}
			Exceptions e = (Exceptions)method.getAttr().get(1);
			assertThat(e.getExceptionTable()[0], is("java.lang.Exception"));
			sds.classfile.attributes.Deprecated dep
					= (sds.classfile.attributes.Deprecated)method.getAttr().get(2);
			assertThat(dep.getType(), is(AttributeType.Deprecated));
			
			Signature sig = (Signature)method.getAttr().get(3);
			assertThat(sig.getSignature(), is("<T:Ljava/lang/Object;>(I)V"));
			
			// runtime visible annotations , and items of that.
			RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)method.getAttr().get(4);
			String depAnn = rva.getAnnotations()[0];
			assertThat(depAnn, is("@java.lang.Deprecated"));
			String rep = rva.getAnnotations()[1];
			assertThat(rep
					, is("@sds.RepeatableRuntimeAnnotation("
							+ "value = {@sds.RuntimeAnnotation(value = \"method\")"
									+ ",@sds.RuntimeAnnotation(value = \"return_type\")})"));
			
			// runtime visible type annotations , and items of that.
			RuntimeVisibleTypeAnnotations rvta
					= (RuntimeVisibleTypeAnnotations)method.getAttr().get(5);
			assertThat(rvta.getTypes()[0].getTargetInfo().getType(), is(TargetInfoType.TypeParameterTarget));
			assertThat(rvta.getTypes()[1].getTargetInfo().getType(), is(TargetInfoType.TypeParameterBoundTarget));
			assertThat(rvta.getTypes()[2].getTargetInfo().getType(), is(TargetInfoType.ThrowsTarget));
			assertThat(rvta.getTypes()[3].getTargetInfo().getType(), is(TargetInfoType.MethodFormalParameterTarget));
			assertThat(rvta.getAnnotations()[0], is("@sds.RuntimeAnnotation(value = \"generics_type_definition\")"));
			assertThat(rvta.getAnnotations()[1], is("@sds.RuntimeAnnotation(value = \"type_param_extends\")"));
			assertThat(rvta.getAnnotations()[2], is("@sds.RuntimeAnnotation(value = \"throws_exception\")"));
			assertThat(rvta.getAnnotations()[3], is("@sds.RuntimeAnnotation(value = \"method_arg\")"));
			
			// runtime visible parameter annotations , and items of that.
			RuntimeVisibleParameterAnnotations ra
					= (RuntimeVisibleParameterAnnotations)method.getAttr().get(6);
			assertThat(ra.getParamAnnotations()[0].getAnnotations()[0]
					, is("@sds.RuntimeAnnotation(value = \"method_arg\")"));
		}
		
		// attributes which class has.
		SourceFile sf = (SourceFile)cfAnnotation.attr.get(0);
		assertThat(sf.getSourceFile(), is("AnnotatedTest.java"));
		
		InnerClasses.Classes[] classes = ((InnerClasses)cfAnnotation.attr.get(2)).getClasses();
		assertThat(classes[0].getNumber("access_flag"), is("public class "));
		assertThat(classes[0].getNumber("inner")      , is("sds.AnnotatedTest$Inner"));
		assertThat(classes[1].getNumber("access_flag"), is("public static final enum "));
		assertThat(classes[1].getNumber("inner")      , is("sds.AnnotatedTest$Type"));
		assertThat(classes[2].getNumber("access_flag"), is("public static final class "));
		assertThat(classes[2].getNumber("inner")      , is("java.lang.invoke.MethodHandles$Lookup"));

		BootstrapMethods.BSM[] bsm = ((BootstrapMethods)cfAnnotation.attr.get(3)).getBSM();
		assertThat(
				bsm[0].getBSMRef()
				, is("java.lang.invoke.LambdaMetafactory.metafactory|("
						+ "java.lang.invoke.MethodHandles$Lookup"
						+ ",java.lang.String"
						+ ",java.lang.invoke.MethodType"
						+ ",java.lang.invoke.MethodType"
						+ ",java.lang.invoke.MethodHandle"
						+ ",java.lang.invoke.MethodType)"
					+ "java.lang.invoke.CallSite"));
		assertThat(bsm[0].getBSMArgs()[0], is("(java.lang.Object)void"));
		assertThat(bsm[0].getBSMArgs()[1], is("sds.AnnotatedTest.lambda$method$0|(java.lang.Object)void"));
	}
}
