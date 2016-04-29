package sds.classfile.attributes;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;

import sds.classfile.attributes.annotation.*;
import sds.classfile.attributes.stackmap.StackMapTable;
import static sds.classfile.attributes.AttributeType.*;

public class AttributeInfoBuilderTest {
	private AttributeInfoBuilder b;

	@Before
	public void set() {
		this.b = AttributeInfoBuilder.getInstance();
	}
	
	@Test
	public void testAttributeBuilder() throws Exception {
		assertThat(b.build(ConstantValue.toString(), 0, 0).getType(), is(ConstantValue));
		assertThat(b.build(Code.toString(), 0, 0).getType()
				, is(Code));
		assertThat(b.build(StackMapTable.toString(), 0, 0).getType()
				, is(StackMapTable));
		assertThat(b.build(Exceptions.toString(), 0, 0).getType()
				, is(Exceptions));
		assertThat(b.build(BootstrapMethods.toString(), 0, 0).getType()
				, is(BootstrapMethods));

		assertThat(b.build(InnerClasses.toString(), 0, 0).getType()
				, is(InnerClasses));
		assertThat(b.build(EnclosingMethod.toString(), 0, 0).getType()
				, is(EnclosingMethod));
		assertThat(b.build(Synthetic.toString(), 0, 0).getType()
				, is(Synthetic));
		assertThat(b.build(Signature.toString(), 0, 0).getType()
				, is(Signature));
		assertThat(b.build(RuntimeVisibleAnnotations.toString(), 0, 0).getType()
				, is(RuntimeVisibleAnnotations));
		assertThat(b.build(RuntimeInvisibleAnnotations.toString(), 0, 0).getType()
				, is(RuntimeInvisibleAnnotations));
		assertThat(b.build(RuntimeVisibleParameterAnnotations.toString(), 0, 0).getType()
				, is(RuntimeVisibleParameterAnnotations));
		assertThat(b.build(RuntimeInvisibleParameterAnnotations.toString(), 0, 0).getType()
				, is(RuntimeInvisibleParameterAnnotations));
		assertThat(b.build(RuntimeVisibleTypeAnnotations.toString(), 0, 0).getType()
				, is(RuntimeVisibleTypeAnnotations));
		assertThat(b.build(RuntimeInvisibleTypeAnnotations.toString(), 0, 0).getType()
				, is(RuntimeInvisibleTypeAnnotations));
		assertThat(b.build(AnnotationDefault.toString(), 0, 0).getType()
				, is(AnnotationDefault));
		assertThat(b.build(MethodParameters.toString(), 0, 0).getType()
				, is(MethodParameters));
		
		assertThat(b.build(SourceFile.toString(), 0, 0).getType()
				, is(SourceFile));
		assertThat(b.build(SourceDebugExtension.toString(), 0, 0).getType()
				, is(SourceDebugExtension));
		assertThat(b.build(LineNumberTable.toString(), 0, 0).getType()
				, is(LineNumberTable));
		assertThat(b.build(LocalVariableTable.toString(), 0, 0).getType()
				, is(LocalVariableTable));
		assertThat(b.build(LocalVariableTypeTable.toString(), 0, 0).getType()
				, is(LocalVariableTypeTable));
		assertThat(b.build(Deprecated.toString(), 0, 0).getType()
				, is(Deprecated));
	}

	@Test(expected = AttributeTypeException.class)
	public void testException() throws Exception {
		b.build("excpetion", 0, 0);
	}
}