package sds;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.MemberInfo;
import sds.classfile.attributes.*;
import sds.classfile.attributes.annotation.*;
import sds.classfile.bytecode.*;
import sds.classfile.constantpool.*;
import static sds.util.AccessFlags.get;
import static sds.util.DescriptorParser.parse;
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
		int sourceIndex = ((SourceFile)cf.attr.getAll()[0]).getSourceFileIndex();
		String source = extract(pool.get(sourceIndex-1), pool);
		assertThat(source, is("Hello.java"));
	}

	@Test
	public void testAssembling2() {
		ConstantPool pool = cfAnnotation.pool;
		Fields f = cfAnnotation.fields;
		MemberInfo m = f.get(0);
		{
			assertThat(get(m.getAccessFlags(), "field"), is("private "));
			String descAndName = extract(m, pool);
			assertThat(descAndName, is("java.lang.String field"));
			
			RuntimeVisibleAnnotations a1 = (RuntimeVisibleAnnotations)m.getAttr().get(0);
			assertThat(parse(extract(pool.get(a1.getAnnotations()[0].getTypeIndex()-1), pool))
					, is("sds.RuntimeAnnotation"));
			assertThat(
					extract(
						pool.get(a1.getAnnotations()[0].getElementValuePairs()[0]
								.getElementNameIndex()-1)
						, pool)
					, is("value"));
			assertThat(
					extract(
						pool.get(a1.getAnnotations()[0].getElementValuePairs()[0]
								.getValue().getConstValueIndex()-1)
						, pool)
					, is("field"));
			RuntimeVisibleTypeAnnotations a2 = (RuntimeVisibleTypeAnnotations)m.getAttr().get(1);
			assertThat(a2.getAnnotations()[0].getTargetInfo().getType().toString(), is("EmptyTarget"));
			assertThat(
					extract(
							pool.get(a2.getAnnotations()[0].getElementValuePairs()[0]
								.getElementNameIndex()-1)
							, pool)
					, is("value"));
			assertThat(
					extract(
							pool.get(a2.getAnnotations()[0].getElementValuePairs()[0]
								.getValue().getConstValueIndex()-1)
							, pool)
					, is("field"));
		}
		
		// public void method(int)	
		MemberInfo method = cfAnnotation.methods.get(1);
		{
			assertThat(get(method.getAccessFlags(), "method"), is("public "));
			assertThat(extract(method, pool), is("(int)void method"));
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
				if(table2[0].getNumber("desc_index") > 0) {
					String desc = extract(pool.get(table2[0].getNumber("desc_index")-1), pool);
					assertThat(desc, is("sds.AnnotatedTest"));
				}
				if(table2[0].getNumber("name_index") > 0) {
					String name = extract(pool.get(table2[0].getNumber("name_index")-1), pool);
					assertThat(name, is("this"));
				}
				
				LocalVariableTypeTable.LVTable[] table3
						= ((LocalVariableTypeTable)code.getAttr().get(2)).getTable();
				assertThat(table3[0].getNumber("start_pc"), is(7));
				assertThat(table3[0].getNumber("length"), is(1));
				assertThat(table3[0].getNumber("index"), is(2));
				if(table3[0].getNumber("desc_index") > 0) {
					String desc = extract(pool.get(table3[0].getNumber("desc_index")-1), pool);
					assertThat(desc, is("java.util.function.Consumer,T"));
				}
				if(table3[0].getNumber("name_index") > 0) {
					String name = extract(pool.get(table3[0].getNumber("name_index")-1), pool);
					assertThat(name, is("c"));
				}
				// runtime visible type annotations , and items of that.
				RuntimeVisibleTypeAnnotations r
						= (RuntimeVisibleTypeAnnotations)code.getAttr().get(3);
				TypeAnnotation ta = r.getAnnotations()[0];
				assertThat(ta.getTargetInfo().getType(), is(TargetInfoType.LocalVarTarget));
				TypePath tp = ta.getTargetPath();
				assertThat(tp.getArgIndex()[0], is(0));
				ElementValuePair evp = ta.getElementValuePairs()[0];
				assertThat(extract(pool.get(evp.getElementNameIndex()-1), pool), is("value"));
				assertThat(extract(pool.get(evp.getValue().getConstValueIndex()-1), pool)
						, is("generics_type"));
			}
			Exceptions e = (Exceptions)method.getAttr().get(1);
			assertThat(extract(pool.get(e.getExceptionIndexTable()[0]-1), pool), is("java.lang.Exception"));
			sds.classfile.attributes.Deprecated dep
					= (sds.classfile.attributes.Deprecated)method.getAttr().get(2);
			assertThat(dep.getType(), is(AttributeType.Deprecated));
			
			Signature sig = (Signature)method.getAttr().get(3);
			assertThat(extract(pool.get(sig.getSignatureIndex()-1), pool)
					, is("<T:Ljava/lang/Object;>(I)V"));
			
			// runtime visible annotations , and items of that.
			RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)method.getAttr().get(4);
			Annotation depAnn = rva.getAnnotations()[0];
			assertThat(parse(extract(pool.get(depAnn.getTypeIndex()-1), pool)), is("java.lang.Deprecated"));
			Annotation rep = rva.getAnnotations()[1];
			assertThat(extract(
						pool.get(
							rep.getElementValuePairs()[0].getValue().getArrayValue()
									.getValues()[0].getAnnotationValue()
									.getElementValuePairs()[0].getValue().getConstValueIndex()-1)
							, pool)
						, is("method"));
			assertThat(extract(
						pool.get(
							rep.getElementValuePairs()[0].getValue().getArrayValue()
									.getValues()[1].getAnnotationValue()
									.getElementValuePairs()[0].getValue().getConstValueIndex()-1)
							, pool)
						, is("return_type"));
			
			// runtime visible type annotations , and items of that.
			RuntimeVisibleTypeAnnotations rvta
					= (RuntimeVisibleTypeAnnotations)method.getAttr().get(5);
			assertThat(rvta.getAnnotations()[0].getTargetInfo().getType()
					, is(TargetInfoType.TypeParameterTarget));
			assertThat(
					extract(
						pool.get(
							rvta.getAnnotations()[0].getElementValuePairs()[0].getValue()
								.getConstValueIndex()-1)
							, pool)
					, is("generics_type_definition"));
			assertThat(rvta.getAnnotations()[1].getTargetInfo().getType()
					, is(TargetInfoType.TypeParameterBoundTarget));
			assertThat(
					extract(
						pool.get(
							rvta.getAnnotations()[1].getElementValuePairs()[0]
								.getValue().getConstValueIndex()-1)
						, pool)
					, is("type_param_extends"));
			assertThat(rvta.getAnnotations()[2].getTargetInfo().getType()
					, is(TargetInfoType.ThrowsTarget));
			assertThat(
					extract(
						pool.get(
							rvta.getAnnotations()[2].getElementValuePairs()[0]
								.getValue().getConstValueIndex()-1)
						, pool)
					, is("throws_exception"));
			assertThat(rvta.getAnnotations()[3].getTargetInfo().getType()
					, is(TargetInfoType.MethodFormalParameterTarget));
			assertThat(
					extract(
						pool.get(
							rvta.getAnnotations()[3].getElementValuePairs()[0]
								.getValue().getConstValueIndex()-1)
						, pool)
					, is("method_arg"));
			
			// runtime visible parameter annotations , and items of that.
			RuntimeVisibleParameterAnnotations ra
					= (RuntimeVisibleParameterAnnotations)method.getAttr().get(6);
			assertThat(parse(extract(pool.get(
					ra.getParamAnnotations()[0].getAnnotations()[0].getTypeIndex()-1), pool))
					, is("sds.RuntimeAnnotation"));
		}
		
		// attributes which class has.
		SourceFile sf = (SourceFile)cfAnnotation.attr.get(0);
		assertThat(extract(pool.get(sf.getSourceFileIndex()-1), pool), is("AnnotatedTest.java"));
		
		InnerClasses.Classes[] classes = ((InnerClasses)cfAnnotation.attr.get(2)).getClasses();
		assertThat(get(classes[0].getNumber("access_flag"), "nested"), is("public class "));
		assertThat(extract(pool.get(classes[0].getNumber("inner")-1), pool)
					, is("sds.AnnotatedTest$Inner"));
		assertThat(get(classes[1].getNumber("access_flag"), "nested")
					, is("public static final enum "));
		assertThat(extract(pool.get(classes[1].getNumber("inner")-1), pool)
					, is("sds.AnnotatedTest$Type"));
		assertThat(get(classes[2].getNumber("access_flag"), "nested")
					, is("public static final class "));
		assertThat(extract(pool.get(classes[2].getNumber("inner")-1), pool)
					, is("java.lang.invoke.MethodHandles$Lookup"));

		BootstrapMethods.BSM[] bsm = ((BootstrapMethods)cfAnnotation.attr.get(3)).getBSM();
		assertThat(
				extract(pool.get(bsm[0].getBSMRef()-1), pool)
				, is("java.lang.invoke.LambdaMetafactory.metafactory("
						+ "java.lang.invoke.MethodHandles$Lookup"
						+ ",java.lang.String"
						+ ",java.lang.invoke.MethodType"
						+ ",java.lang.invoke.MethodType"
						+ ",java.lang.invoke.MethodHandle"
						+ ",java.lang.invoke.MethodType)"
					+ "java.lang.invoke.CallSite"));
		assertThat(extract(pool.get(bsm[0].getBSMArgs()[0]-1), pool), is("(java.lang.Object)void"));
		assertThat(extract(pool.get(bsm[0].getBSMArgs()[1]-1), pool)
				, is("sds.AnnotatedTest.lambda$method$0(java.lang.Object)void"));
	}
}