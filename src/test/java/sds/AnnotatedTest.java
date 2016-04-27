package sds;

import java.util.function.Consumer;

@RuntimeAnnotation("class")
public class AnnotatedTest {

	@RuntimeAnnotation("enum")
	public enum Type {
		@RuntimeAnnotation("enum_element")
		test_annotation
	};

	@RuntimeAnnotation("field")
	private String field;

	@RuntimeAnnotation("constructor")
	public AnnotatedTest() {}

	@RuntimeAnnotation("method")
	public
	<@RuntimeAnnotation("generics_type_definition") T extends
		@RuntimeAnnotation("type_param_extends") Object>
	@RuntimeAnnotation("return_type") void
	method(@RuntimeAnnotation("method_arg") int arg)
	throws @RuntimeAnnotation("throws_exception") Exception {
		@RuntimeAnnotation("local_variable")
		Consumer<@RuntimeAnnotation("generics_type") T> c;
		c = (@RuntimeAnnotation("lambda_arg")T t) -> testMethod(t);
	}

	public <T> void testMethod(T t) {}

	public <@RuntimeAnnotation("type_param") T> void typeParam1() {
		
	}

	public <T extends @RuntimeAnnotation("type_param_extends") Object>
	void typeParam2() {
		
	}
}