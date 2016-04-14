package sds.classfile.attributes;

/**
 * This enum class is for type of {@link AttributeInfo <code>AttributeInfo</code>}.
 * @author inagaki
 */
public enum AttributeType {
	ConstantValue,
	Code,
	StackMapTable,
	Exceptions,
	BootstrapMethods,
	
	InnerClasses,
	EnclosingMethod,
	Synthetic,
	Signature,
	RuntimeVisibleAnnotations,
	RuntimeInvisibleAnnotations,
	RuntimeVisibleParameterAnnotations,
	RuntimeInvisibleParameterAnnotations,
	RuntimeVisibleTypeAnnotations,
	RuntimeInvisibleTypeAnnotations,
	AnnotationDefault,
	MethodParameters,
	
	SourceFile,
	SourceDebugExtension,
	LineNumberTable,
	LocalVariableTable,
	LocalVariableTypeTable,
	Deprecated;
}
