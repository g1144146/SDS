package sds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD, ElementType.CONSTRUCTOR
		, ElementType.LOCAL_VARIABLE, ElementType.METHOD
		, ElementType.TYPE_PARAMETER, ElementType.TYPE
		, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatableRuntimeAnnotation {
	public RuntimeAnnotation[] value();
}