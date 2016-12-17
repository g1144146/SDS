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
 * This factory class is for {@link AttributeInfo <code>AttributeInfo</code>}.
 * @author inagaki
 */
public class AttributeInfoFactory {
	/**
	 * returns attribute info.
	 * @param attrName attribute name
	 * @param length attribute length.
	 * @return attribute info
	 * @throws AttributeTypeException 
	 */
	public AttributeInfo create(String attrName, int length) throws AttributeTypeException {
		switch(attrName) {
			case "AnnotationDefault":
				return new AnnotationDefault();
			case "BootstrapMethods":
				return new BootstrapMethods();
			case "Code":
				return new Code();
			case "ConstantValue":
				return new ConstantValue();
			case "Deprecated":
				return new Deprecated();
			case "EnclosingMethod":
				return new EnclosingMethod();
			case "Exceptions":
				return new Exceptions();
			case "InnerClasses":
				return new InnerClasses();
			case "LineNumberTable":
				return new LineNumberTable();
			case "LocalVariableTable":
				return new LocalVariableTable();
			case "LocalVariableTypeTable":
				return new LocalVariableTypeTable();
			case "MethodParameters":
				return new MethodParameters();
			case "RuntimeInvisibleAnnotations":
				return new RuntimeInvisibleAnnotations();
			case "RuntimeInvisibleParameterAnnotations":
				return new RuntimeInvisibleParameterAnnotations();
			case "RuntimeInvisibleTypeAnnotations":
				return new RuntimeInvisibleTypeAnnotations();
			case "RuntimeVisibleAnnotations":
				return new RuntimeVisibleAnnotations();
			case "RuntimeVisibleParameterAnnotations":
				return new RuntimeVisibleParameterAnnotations();
			case "RuntimeVisibleTypeAnnotations":
				return new RuntimeVisibleTypeAnnotations();
			case "Signature":
				return new Signature();
			case "SourceDebugExtension":
				return new SourceDebugExtension(length);
			case "SourceFile":
				return new SourceFile();
			case "Synthetic":
				return new Synthetic();
			case "StackMapTable":
				return new StackMapTable();
			default:
				throw new AttributeTypeException(attrName);
		}
	}
}