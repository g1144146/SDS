package sophomore.classfile.attributes;

/**
 *
 * @author inagaki
 */
public enum AttributeType {
	ConstantValue("ConstantValue"),
	Code("Code"),
	StackMapTable("StackMapTable"),
	Exceptions("Exceptions"),
	BootstrapMethods("BootstrapMethods"),
	
	InnerClasses("InnerClasses"),
	EnclosingMethod("EnclosingMethod"),
	Synthetic("Synthetic"),
	Signature("Signature"),
	RuntimeVisibleAnnotations("RuntimeVisibleAnnotations"),
	RuntimeInvisibleAnnotations("RuntimeInvisibleAnnotations"),
	RuntimeVisibleParameterAnnotations("RuntimeVisibleParameterAnnotations"),
	RuntimeInvisibleParameterAnnotations("RuntimeInvisibleParameterAnnotations"),
	RuntimeVisibleTypeAnnotations("RuntimeVisibleTypeAnnotations"),
	RuntimeInvisibleTypeAnnotations("RuntimeInvisibleTypeAnnotations"),
	AnnotationDefault("AnnotationDefault"),
	MethodParameters("MethodParameters"),
	
	SourceFile("SourceFile"),
	SourceDebugExtension("SourceDebugExtension"),
	LineNumberTable("LineNumberTable"),
	LocalVariableTable("LocalVariableTable"),
	LocalVariableTypeTable("LocalVariableTypeTable"),
	Deprecated("Deprecated");

	/**
	 * 
	 */
	private String type;

	AttributeType(String type) {
		this.type = type;
	}
	
	/**
	 *
	 * @return
	 */
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
}
