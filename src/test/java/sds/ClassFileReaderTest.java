package sds;

import java.util.*;
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
import sds.classfile.constantpool.*;
import sds.util.*;
import static sds.util.AccessFlags.get;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

public class ClassFileReaderTest {
	private ClassFile cf;
	private ClassFile cfAnnotation;
	
	@Before
	public void startUp() {
		String filePath = generateFilePath("build" , "resources"
					, "test", "resources", "Hello.class");
		ClassFileReader reader = new ClassFileReader(filePath); 
//		reader.read();
//		this.cf = reader.getClassFile();

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

//	@Test
	public void testAssembling() throws Exception {
		assertThat(Integer.toHexString(cf.magicNumber), is("cafebabe"));
		assertThat(cf.majorVersion, is(52));
		assertThat(cf.minorVersion, is(0));
		assertThat(AccessFlags.get(cf.accessFlag, "class"), is("public class "));
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
		assertThat(get(m.getAccessFlags(), "field"), is("private "));
		String descAndName = extract(m, pool);
		assertThat(descAndName, is("java.lang.String field"));
		
		RuntimeVisibleAnnotations a1 = (RuntimeVisibleAnnotations)m.getAttr().get(0);
		assertThat(parse(extract(pool.get(a1.getAnnotations()[0].getTypeIndex()-1), pool))
				, is("sds.RuntimeAnnotation"));
		assertThat(
			extract(
				pool.get(a1.getAnnotations()[0].getElementValuePairs()[0].getElementNameIndex()-1)
				, pool)
			, is("value"));
		assertThat(
			extract(
				pool.get(a1.getAnnotations()[0].getElementValuePairs()[0].getValue().getConstValueIndex()-1)
				, pool)
			, is("field"));
		RuntimeVisibleTypeAnnotations a2 = (RuntimeVisibleTypeAnnotations)m.getAttr().get(1);
		assertThat(a2.getAnnotations()[0].getTargetInfo().getType().toString(), is("EmptyTarget"));
		assertThat(
			extract(
				pool.get(a2.getAnnotations()[0].getElementValuePairs()[0].getElementNameIndex()-1)
				, pool)
			, is("value"));
		assertThat(
			extract(
				pool.get(a2.getAnnotations()[0].getElementValuePairs()[0].getValue().getConstValueIndex()-1)
				, pool)
			, is("field"));
		
		MemberInfo method = cfAnnotation.methods.get(1);
		assertThat(get(method.getAccessFlags(), "method"), is("public "));
		assertThat(extract(method, pool), is("(int,)void method"));
	}
}