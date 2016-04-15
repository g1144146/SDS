package sds.assemble;

import java.util.HashMap;
import java.util.Map;

import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.ConstantValue;
import sds.classfile.attributes.EnclosingMethod;
import sds.classfile.attributes.Exceptions;
import sds.classfile.attributes.InnerClasses;
import sds.classfile.attributes.LineNumberTable;
import sds.classfile.attributes.LocalVariable;
import sds.classfile.attributes.LocalVariableTable;
import sds.classfile.attributes.LocalVariableTypeTable;
import sds.classfile.attributes.MethodParameters;
import sds.classfile.attributes.Signature;
import sds.classfile.attributes.SourceDebugExtension;
import sds.classfile.attributes.annotation.Annotation;
import sds.classfile.attributes.annotation.AnnotationDefault;
import sds.classfile.attributes.annotation.ElementValuePair;
import sds.classfile.attributes.annotation.ParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleParameterAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.TypeAnnotation;
import sds.classfile.attributes.stackmap.StackMapFrame;
import sds.classfile.attributes.stackmap.StackMapTable;
import sds.util.Utf8ValueExtractor;

/**
 *
 * @author inagakikenichi
 */
public abstract class BaseContent {
	boolean hasAnnotation;
	Map<String, String> genericsMap = new HashMap<>();

	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case AnnotationDefault:
				AnnotationDefault ad = (AnnotationDefault)info;
//				printElementValue(ad.getDefaultValue());
				break;
			case ConstantValue:
//				ConstantValue cv = (ConstantValue)info;
//				int index = cv.getConstantValueIndex();
//				out.println("\tconstant_value"+Utf8ValueExtractor.extract(pool.get(index-1)));
				break;
			case Deprecated:
				// do nothing.
				break;
			case LocalVariableTable:
				LocalVariableTable lvt = (LocalVariableTable)info;
//				for(LocalVariable.LVTable t : lvt.getTable()) {
//					out.println("\tpc    : " + t.getNumber("start_pc")
//							+ "-" + (t.getNumber("start_pc")+t.getNumber("length")-1));
//					out.println("\tname  : " + Utf8ValueExtractor.extract(pool.get(t.getNumber("name_index")-1)));
//					out.println("\tdesc  : "
//							+ DescriptorParser.parse(Utf8ValueExtractor.extract(pool.get(t.getNumber("descriptor")-1))));
//					out.println("\tindex : " + t.getNumber("index"));
//				}
				break;
			case LocalVariableTypeTable:
				LocalVariableTypeTable lvtt = (LocalVariableTypeTable)info;
//				for(LocalVariable.LVTable t : lvtt.getTable()) {
//					out.println("\tpc    : " + t.getNumber("start_pc")
//							+ "-" + (t.getNumber("start_pc")+t.getNumber("length")-1));
//					out.println("\tname  : " + Utf8ValueExtractor.extract(pool.get(t.getNumber("name_index")-1)));
//					out.println("\tdesc  : "
//							+ DescriptorParser.parse(Utf8ValueExtractor.extract(pool.get(t.getNumber("descriptor")-1))));
//					out.println("\tindex : " + t.getNumber("index"));
//				}
				break;
			case MethodParameters:
				MethodParameters mp = (MethodParameters)info;
//				for(MethodParameters.Parameters p : mp.getParams()) {
//					String flag = AccessFlags.get(p.getAccessFlag(), "method");
//					out.println(flag + Utf8ValueExtractor.extract(pool.get(p.getNameIndex()-1)));
//				}
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations)info;
//				for(Annotation a : ria.getAnnotations()) {
//					printAnnotation(a);
//				}
				break;
			case RuntimeInvisibleParameterAnnotations:
				RuntimeInvisibleParameterAnnotations ripa = (RuntimeInvisibleParameterAnnotations)info;
//				for(ParameterAnnotations pa : ripa.getParamAnnotations()) {
//					for(Annotation a : pa.getAnnotations()) {
//						printAnnotation(a);
//					}
//				}
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
//				for(TypeAnnotation pa : rita.getAnnotations()) {
//					printTargetInfo(pa.getTargetInfo());
//					printTypePath(pa.getTargetPath());
//					for(ElementValuePair evp : pa.getElementValuePairs()) {
//						printElementValuePair(evp);
//					}
//				}
				break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
//				for(Annotation a : rva.getAnnotations()) {
//					printAnnotation(a);
//				}
				break;
			case RuntimeVisibleParameterAnnotations:
				RuntimeVisibleParameterAnnotations rvpa = (RuntimeVisibleParameterAnnotations)info;
//				for(ParameterAnnotations pa : rvpa.getParamAnnotations()) {
//					for(Annotation a : pa.getAnnotations()) {
//						printAnnotation(a);
//					}
//				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
//				for(TypeAnnotation pa : rvta.getAnnotations()) {
//					printTargetInfo(pa.getTargetInfo());
//					printTypePath(pa.getTargetPath());
//					for(ElementValuePair e : pa.getElementValuePairs()) {
//						printElementValuePair(e);
//					}
//				}
				break;
			case Signature:
				Signature sig = (Signature)info;
				String signature
					= Utf8ValueExtractor.extract(pool.get(sig.getSignatureIndex()-1), pool);
				signature = signature.substring(signature.indexOf("<"), signature.indexOf(">"));
				String key = null, value = null;
				for(String s : signature.split(":")) {
					if(s.length() != 0) {
						if(key == null) {
							key = s;
						} else if(value == null) {
							value = s;
							break;
						}
					}
				}
				genericsMap.put(key, value);
				break;
			case SourceDebugExtension:
				SourceDebugExtension sde = (SourceDebugExtension)info;
				break;
			case Synthetic:
				// do nothing.
				break;
			default:
				break;
		}
	}
}
