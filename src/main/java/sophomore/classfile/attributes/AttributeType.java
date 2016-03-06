package sophomore.classfile.attributes;

/**
 *
 * @author inagaki
 */
public class AttributeType {
	/**
	 * 
	 */
	public static enum Type {
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
		
		private String type;
		Type(String type) {
			this.type = type;
		}

		/**
		 * 
		 * @return 
		 */
		public String getType() {
			return type;
		}
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws UnknownAttributeTypeException 
	 */
	public static AttributeType.Type get(String type) throws UnknownAttributeTypeException {
		switch(type) {
			case "ConstantValue":
				return Type.ConstantValue;
			case "Code":
				return Type.Code;
			case "StackMapTable":
				return Type.StackMapTable;
			case "Exceptions":
				return Type.Exceptions;
			case "BootstrapMethods":
				return Type.BootstrapMethods;
			case "InnerClasses":
				return Type.InnerClasses;
			case "EnclosingMethod":
				return Type.EnclosingMethod;
			case "Synthetic":
				return Type.Synthetic;
			case "Signature":
				return Type.Signature;
			case "RuntimeVisibleAnnotations":
				return Type.RuntimeVisibleAnnotations;
			case "RuntimeInvisibleAnnotations":
				return Type.RuntimeInvisibleAnnotations;
			case "RuntimeVisibleParameterAnnotations":
				return Type.RuntimeVisibleParameterAnnotations;
			case "RuntimeInvisibleParameterAnnotations":
				return Type.RuntimeInvisibleParameterAnnotations;
			case "RuntimeVisibleTypeAnnotations":
				return Type.RuntimeVisibleTypeAnnotations;
			case "RuntimeInvisibleTypeAnnotations":
				return Type.RuntimeInvisibleTypeAnnotations;
			case "AnnotationDefault":
				return Type.AnnotationDefault;
			case "MethodParameters":
				return Type.MethodParameters;
			case "SourceFile":
				return Type.SourceFile;
			case "SourceDebugExtension":
				return Type.SourceDebugExtension;
			case "LineNumberTable":
				return Type.LineNumberTable;
			case "LocalVariableTable":
				return Type.LocalVariableTable;
			case "LocalVariableTypeTable":
				return Type.LocalVariableTypeTable;
			case "Deprecated":
				return Type.Deprecated;
			default:
				System.out.println("*** unknown attribute type ***");
		}
		throw new UnknownAttributeTypeException(type);
	}
 }
