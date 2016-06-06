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
import sds.classfile.attributes.annotation.TargetInfo;
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
	private AnnotationContent visibleAnnotation;
	private AnnotationContent invisibleAnnotation;
	private TypeAnnotationContent visibleTypeAnnotation;
	private TypeAnnotationContent invisibleTypeAnnotation;
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
				this.visibleAnnotation = new AnnotationContent(rva.getAnnotations(), pool);
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations)info;
				this.invisibleAnnotation = new AnnotationContent(ria.getAnnotations(), pool);
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				this.hasAnnotation = true;
				this.visibleTypeAnnotation = new TypeAnnotationContent(rvta.getAnnotations(), pool);
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				this.invisibleTypeAnnotation = new TypeAnnotationContent(rita.getAnnotations(), pool);
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
	 * returns visible annotations content.
	 * @return visible annotation content
	 */
	public AnnotationContent getVisibleAnnotation() {
		return visibleAnnotation;
	}

	/**
	 * returns invisible annotations content.
	 * @return invisible annotation content
	 */
	public AnnotationContent getInvisibleAnnotation() {
		return invisibleAnnotation;
	}

	/**
	 * returns visible type annotations content.
	 * @return visible type annotation content
	 */
	public TypeAnnotationContent getVisibleTypeAnnotation() {
		return visibleTypeAnnotation;
	}

	/**
	 * returns invisible type annotations content.
	 * @return invisible type annotation content
	 */
	public TypeAnnotationContent getInvisibleTypeAnnotation() {
		return invisibleTypeAnnotation;
	}

	// <editor-fold defaultstate="collapsed" desc="[class] AnnotationContent">
	/**
	 * This class is for annotations of class and member.
	 */
	public class AnnotationContent extends AbstractAnnotationContent {
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
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="[class] TypeAnnotationContent">
	/**
	 * This class is for type annotations of class and member.
	 */
	public class TypeAnnotationContent extends AbstractAnnotationContent {
		private TargetInfo[] targets;

		TypeAnnotationContent(TypeAnnotation[] ta, ConstantPool pool) {
			this.annotations = new String[ta.length];
			this.targets = new TargetInfo[ta.length];
			try {
				for(int i = 0; i < ta.length; i++) {
					annotations[i] = parseAnnotation(ta[i], new StringBuilder(), pool);
					targets[i] = ta[i].getTargetInfo();
				}
			} catch(ElementValueException e) {
				e.printStackTrace();
			}
		}

		/**
		 * returns target info of annotation.
		 * @return target info
		 */
		public TargetInfo[] getTargets() {
			return targets;
		}
	}
	// </editor-fold>
}