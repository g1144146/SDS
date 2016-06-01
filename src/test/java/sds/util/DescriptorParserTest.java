package sds.util;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.hamcrest.Matchers.is;

import static sds.util.DescriptorParser.parse;
import static sds.util.DescriptorParser.parseImportClass;

public class DescriptorParserTest {
	@Test
	public void testParse() {
		assertThat(parse("(Ljava/lang/System;)V"), is("(java.lang.System)void"));
		assertThat(parse("(IID)Ljava/lang/reflect/Method;")
				, is("(int,int,double)java.lang.reflect.Method"));
		assertThat(parse("(BCDFIJSZ)V")
				, is("(byte,char,double,float,int,long,short,boolean)void"));
		assertThat(parse("Lorg/objectweb/asm/MethodVisitor;")
				, is("org.objectweb.asm.MethodVisitor"));
		assertThat(parse("(TT;BCTR;)TS;"), is("(T,byte,char,R)S"));
		assertThat(parse("(Ljava/lang/invoke/MethodHandles$Lookup;"
							+ "Ljava/lang/String;"
							+ "Ljava/lang/invoke/MethodType;"
							+ "Ljava/lang/invoke/MethodType;"
							+ "Ljava/lang/invoke/MethodHandle;"
							+ "Ljava/lang/invoke/MethodType;)"
						+ "Ljava/lang/invoke/CallSite;")
				, is("(java.lang.invoke.MethodHandles$Lookup"
							+ ",java.lang.String,java.lang.invoke.MethodType"
							+ ",java.lang.invoke.MethodType"
							+ ",java.lang.invoke.MethodHandle"
							+ ",java.lang.invoke.MethodType)"
						+ "java.lang.invoke.CallSite"));
	}

	@Test
	public void testParseImport() {
		assertArrayEquals(
				parseImportClass("(Ljava/lang/System;)V")
				, new String[]{});
		assertArrayEquals(
				parseImportClass("(IID)Ljava/lang/reflect/Method;")
				, new String[]{"java.lang.reflect.Method"});
		assertArrayEquals(
				parseImportClass("(Ljava/lang/invoke/MethodHandles$Lookup;"
							+ "Ljava/lang/String;"
							+ "Ljava/lang/invoke/MethodType;"
							+ "Ljava/lang/invoke/MethodType;"
							+ "Ljava/lang/invoke/MethodHandle;"
							+ "Ljava/lang/invoke/MethodType;)"
						+ "Ljava/lang/invoke/CallSite;")
				, new String[]{
					"java.lang.invoke.MethodHandles$Lookup"
					, "java.lang.invoke.MethodType"
					, "java.lang.invoke.MethodHandle"
					, "java.lang.invoke.CallSite"
				});
	}
}
