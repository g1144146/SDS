package sds.assemble;

import java.util.HashMap;
import java.util.Map;

import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.Signature;
import sds.classfile.attributes.annotation.Annotation;
import sds.classfile.attributes.annotation.ElementValueException;
import sds.classfile.attributes.annotation.RuntimeInvisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.TypeAnnotation;

import static sds.util.AnnotationParser.parseAnnotation;
import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

/**
 * This adapter class is for
 * {@link ClassContent <code>ClassContent</code>}
 * and
 * {@link MemberContent <code>MemberContent</code>}.
 * @author inagaki
 */
public abstract class BaseContent {
	private boolean hasAnnotation;
	private Map<String, String> genericsMap = new HashMap<>();
	private AnnotationContent annContent;
	private TypeAnnotationContent taContent;
	Type contentType;
	public static enum Type {
		Class,
		Nested,
		Method,
		Code_In_Method,
		Field;
	}

	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case Deprecated: break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
				this.annContent = new AnnotationContent(rva.getAnnotations(), pool);
//				for(Annotation a : rva.getAnnotations()) {
//					printAnnotation(a);
//				}
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations)info;
//				for(Annotation a : ria.getAnnotations()) {
//					printAnnotation(a);
//				}
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				this.hasAnnotation = true;
				this.taContent = new TypeAnnotationContent(rvta.getAnnotations(), pool);
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
			case Signature:
				if(contentType != Type.Field) {
					Signature sig = (Signature)info;
					String desc = extract(pool.get(sig.getSignatureIndex()-1), pool);
					System.out.println(desc);
					String parsedSig = parse(desc, true);
					String genericsType = parsedSig.substring(1, parsedSig.lastIndexOf(">"));
					for(String type : genericsType.split(",")) {
						String[] typeAndExtends = type.split(" extends ");
						genericsMap.put(typeAndExtends[0], typeAndExtends[1]);
					}
				}
				break;
			case Synthetic: break;
			default:        break;
		}
	}

	/**
	 * return generics map.
	 * @return map
	 */
	public Map<String, String> getGenericsMap() {
		return genericsMap;
	}

	/**
	 * returns type annotations content.
	 * @return annotations
	 */
	public TypeAnnotationContent getTAContent() {
		return taContent;
	}

	public class AnnotationContent {
		private String[] annotations;
		AnnotationContent(Annotation[] annotations, ConstantPool pool) {
			this.annotations = new String[annotations.length];
			try {
				for(int i = 0; i < annotations.length; i++) {
					this.annotations[i] = parseAnnotation(annotations[i], new StringBuilder(), pool);
				}
			} catch(ElementValueException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This class is for type annotations of class and member.
	 */
	public class TypeAnnotationContent {
		TypeAnnotationContent(TypeAnnotation[] ta, ConstantPool pool) {
			StringBuilder sb = new StringBuilder();
			try {
				for(TypeAnnotation t : ta) {
					sb.append(parseAnnotation(t, new StringBuilder(), pool));
				}
			} catch(ElementValueException e) {
				e.printStackTrace();
			}
		}
	}
}