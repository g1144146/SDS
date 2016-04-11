package sds.classfile.attributes;

import sds.classfile.attributes.annotation.AnnotationDefault;
import sds.classfile.attributes.annotation.RuntimeInvisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.stackmap.StackMapTable;

/**
 *
 * @author inagaki
 */
public class AttributeInfoBuilder {
	/**
	 * 
	 */
	private static AttributeInfoBuilder builder = null;

	/**
	 * 
	 * @return 
	 */
	public static AttributeInfoBuilder getInstance() {
		if(builder == null) {
			builder = new AttributeInfoBuilder();
		}
		return builder;
	}

	/**
	 * 
	 * @param attrName
	 * @param nameIndex
	 * @param length
	 * @return
	 * @throws AttributeTypeException 
	 */
	public AttributeInfo build(String attrName, int nameIndex, int length)
	throws AttributeTypeException {
		switch(attrName) {
			case "AnnotationDefault":
				return new AnnotationDefault(nameIndex, length);
			case "BootstrapMethods":
				return new BootstrapMethods(nameIndex, length);
			case "Code":
				return new Code(nameIndex, length);
			case "ConstantValue":
				return new ConstantValue(nameIndex, length);
			case "Deprecated":
				return new Deprecated(nameIndex, length);
			case "EnclosingMethod":
				return new EnclosingMethod(nameIndex, length);
			case "Exceptions":
				return new Exceptions(nameIndex, length);
			case "InnerClasses":
				return new InnerClasses(nameIndex, length);
			case "LineNumberTable":
				return new LineNumberTable(nameIndex, length);
			case "LocalVariableTable":
				return new LocalVariableTable(nameIndex, length);
			case "LocalVariableTypeTable":
				return new LocalVariableTypeTable(nameIndex, length);
			case "MethodParameters":
				return new MethodParameters(nameIndex, length);
			case "RuntimeInvisibleAnnotations":
				return new RuntimeInvisibleAnnotations(nameIndex, length);
			case "RuntimeInvisibleParameterAnnotations":
				return new RuntimeInvisibleParameterAnnotations(nameIndex, length);
			case "RuntimeInvisibleTypeAnnotations":
				return new RuntimeInvisibleTypeAnnotations(nameIndex, length);
			case "RuntimeVisibleAnnotations":
				return new RuntimeVisibleAnnotations(nameIndex, length);
			case "RuntimeVisibleParameterAnnotations":
				return new RuntimeVisibleParameterAnnotations(nameIndex, length);
			case "RuntimeVisibleTypeAnnotations":
				return new RuntimeVisibleTypeAnnotations(nameIndex, length);
			case "Signature":
				return new Signature(nameIndex, length);
			case "SourceDebugExtension":
				return new SourceDebugExtension(nameIndex, length);
			case "SourceFile":
				return new SourceFile(nameIndex, length);
			case "Synthetic":
				return new Synthetic(nameIndex, length);
			case "StackMapTable":
				return new StackMapTable(nameIndex, length);
			default:
				throw new AttributeTypeException(attrName);
		}
	}
}
