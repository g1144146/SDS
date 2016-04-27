package sds;

import java.util.*;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import org.hamcrest.Matchers;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.constantpool.ConstantInfo;
import sds.util.AccessFlags;
import sds.util.Utf8ValueExtractor;

public class ClassFileReaderTest {
	private ClassFile cf;
	private ClassFile cfAnnotation;
	
	@Before
	public void startUp() {
		String filePath = generateFilePath("build" , "resources"
					, "test", "resources", "Hello.class");
		ClassFileReader reader = new ClassFileReader(filePath); 
		reader.read();
		this.cf = reader.getClassFile();

//		String path = generateFilePath("build", "classes", "test"
//					, "sds", "AnnotatedTest.class");
//		ClassFileReader r = new ClassFileReader(path); 
//		r.read();
//		this.cfAnnotation = r.getClassFile();
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
		Assert.assertThat(Integer.toHexString(cf.magicNumber), Matchers.is("cafebabe"));
		Assert.assertThat(cf.majorVersion, Matchers.is(52));
		Assert.assertThat(cf.minorVersion, Matchers.is(0));
		Assert.assertThat(AccessFlags.get(cf.accessFlag, "class"), Matchers.is("public class "));
		Assert.assertThat(cf.fields.getAll(), Matchers.is(new MemberInfo[0]));
		Assert.assertThat(cf.interfaces, Matchers.is(new int[0]));
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
		Assert.assertThat(Utf8ValueExtractor.extract(pool.get(cf.superClass-1), pool)
				, Matchers.is("java.lang.Object"));
		Assert.assertThat(Utf8ValueExtractor.extract(pool.get(cf.thisClass-1), pool)
				, Matchers.is("Hello"));
	}
}