package sds.classfile.constantpool;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import static sds.classfile.constantpool.ConstantType.*;

public class ConstantInfoBuilderTest {
	private ConstantInfoBuilder b;

	@Before
	public void set() {
		this.b = ConstantInfoBuilder.getInstance();
	}
	
	@Test
	public void testAttributeBuilder() throws Exception {
		assertThat(b.build(C_CLASS).getTag(), is(C_CLASS));
		assertThat(b.build(C_DOUBLE).getTag(), is(C_DOUBLE));
		assertThat(b.build(C_FIELDREF).getTag(), is(C_FIELDREF));
		assertThat(b.build(C_FLOAT).getTag(), is(C_FLOAT));
		assertThat(b.build(C_INTEGER).getTag(), is(C_INTEGER));
		assertThat(b.build(C_INTERFACE_METHODREF).getTag(), is(C_INTERFACE_METHODREF));
		assertThat(b.build(C_INVOKE_DYNAMIC).getTag(), is(C_INVOKE_DYNAMIC));
		assertThat(b.build(C_LONG).getTag(), is(C_LONG));
		assertThat(b.build(C_METHOD_HANDLE).getTag(), is(C_METHOD_HANDLE));
		assertThat(b.build(C_METHOD_TYPE).getTag(), is(C_METHOD_TYPE));
		assertThat(b.build(C_METHODREF).getTag(), is(C_METHODREF));
		assertThat(b.build(C_NAME_AND_TYPE).getTag(), is(C_NAME_AND_TYPE));
		assertThat(b.build(C_STRING).getTag(), is(C_STRING));
		assertThat(b.build(C_UTF8).getTag(), is(C_UTF8));
	}
}