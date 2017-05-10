package sds.classfile.attributes;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;

import sds.classfile.attributes.stackmap.StackMapTable;
import static sds.classfile.attributes.AttributeType.*;

public class AttributeInfoFactoryTest {
	private AttributeInfoFactory b;

	@Before
	public void set() {
		this.b = new AttributeInfoFactory();
	}
	
	@Test
	public void testAttributeBuilder() throws Exception {
//		assertThat(b.create(ConstantValue.toString(), 0).getType(), is(ConstantValue));
//		assertThat(b.create(Code.toString(), 0).getType(), is(Code));
//		assertThat(b.create(StackMapTable.toString(), 0).getType(), is(StackMapTable));
//		assertThat(b.create(Exceptions.toString(), 0).getType(), is(Exceptions));
//		assertThat(b.create(BootstrapMethods.toString(), 0).getType(), is(BootstrapMethods));
//
//		assertThat(b.create(InnerClasses.toString(), 0).getType(), is(InnerClasses));
//		assertThat(b.create(EnclosingMethod.toString(), 0).getType(), is(EnclosingMethod));
//		assertThat(b.create(Synthetic.toString(), 0).getType(), is(Synthetic));
//		assertThat(b.create(Signature.toString(), 0).getType(), is(Signature));
//		assertThat(b.create(RuntimeVisibleAnnotations.toString(), 0).getType(), is(RuntimeVisibleAnnotations));
//		assertThat(b.create(RuntimeInvisibleAnnotations.toString(), 0).getType(), is(RuntimeInvisibleAnnotations));
//		assertThat(b.create(RuntimeVisibleParameterAnnotations.toString(), 0).getType()
//				, is(RuntimeVisibleParameterAnnotations));
//		assertThat(b.create(RuntimeInvisibleParameterAnnotations.toString(), 0).getType()
//				, is(RuntimeInvisibleParameterAnnotations));
//		assertThat(b.create(RuntimeVisibleTypeAnnotations.toString(), 0).getType(), is(RuntimeVisibleTypeAnnotations));
//		assertThat(b.create(RuntimeInvisibleTypeAnnotations.toString(), 0).getType()
//				, is(RuntimeInvisibleTypeAnnotations));
//		assertThat(b.create(AnnotationDefault.toString(), 0).getType(), is(AnnotationDefault));
//		assertThat(b.create(MethodParameters.toString(), 0).getType(), is(MethodParameters));
//		
//		assertThat(b.create(SourceFile.toString(), 0).getType(), is(SourceFile));
//		assertThat(b.create(SourceDebugExtension.toString(), 0).getType(), is(SourceDebugExtension));
//		assertThat(b.create(LineNumberTable.toString(), 0).getType(), is(LineNumberTable));
//		assertThat(b.create(LocalVariableTable.toString(), 0).getType(), is(LocalVariableTable));
//		assertThat(b.create(LocalVariableTypeTable.toString(), 0).getType(), is(LocalVariableTypeTable));
//		assertThat(b.create(Deprecated.toString(), 0).getType(), is(Deprecated));
	}

//	@Test(expected = AttributeTypeException.class)
//	public void testException() throws Exception {
//		b.create("excpetion", 0);
//	}
}