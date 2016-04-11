package sds.classfile.attributes;

/**
 *
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
