package sds.assemble;

import java.util.HashMap;
import java.util.Map;

import sds.classfile.ConstantPool;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.Signature;
import sds.classfile.attributes.annotation.RuntimeInvisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeInvisibleTypeAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleAnnotations;
import sds.classfile.attributes.annotation.RuntimeVisibleTypeAnnotations;
import sds.classfile.attributes.annotation.TargetInfo;

import static sds.util.DescriptorParser.parse;

/**
 * This adapter class is for
 * {@link ClassContent <code>ClassContent</code>}
 * and
 * {@link MemberContent <code>MemberContent</code>}.
 * @author inagaki
 */
public abstract class BaseContent {
	private Map<String, String> genericsMap = new HashMap<>();
	private AnnotationContent annContent;
	TypeAnnotationContent taContent;
	boolean hasAnnotation;

	/**
	 * analyzes classfile attribute.
	 * @param info attribute
	 * @param pool constant-pool
	 */
	public void analyzeAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case Deprecated: break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
				this.annContent = new AnnotationContent(rva.getAnnotations(), true);
//				System.out.println("<Runtime Visible Annotation>: ");
//				for(String a : annContent.visible)
//					System.out.println("  " + a);
				break;
			case RuntimeInvisibleAnnotations:
				RuntimeInvisibleAnnotations ria = (RuntimeInvisibleAnnotations)info;
				if(annContent == null) {
					this.annContent = new AnnotationContent(ria.getAnnotations(), false);
				} else {
					annContent.setInvisible(ria.getAnnotations());
				}
//				System.out.println("<Runtime Invisible Annotation>: ");
//				for(String a : annContent.invisible)
//					System.out.println("  " + a);
				break;
			case RuntimeVisibleTypeAnnotations:
				RuntimeVisibleTypeAnnotations rvta = (RuntimeVisibleTypeAnnotations)info;
				this.taContent = new TypeAnnotationContent(rvta.getAnnotations(), true);
				System.out.println("<Runtime Visible Type Annotation>: ");
				for(String a : taContent.visible)
					System.out.println("  " + a);
				break;
			case RuntimeInvisibleTypeAnnotations:
				RuntimeInvisibleTypeAnnotations rita = (RuntimeInvisibleTypeAnnotations)info;
				if(taContent == null) {
					this.taContent = new TypeAnnotationContent(rita.getAnnotations(), false);
				} else {
					taContent.setInvisible(rita.getAnnotations());
				}
				System.out.println("<Runtime Invisible Type Annotation>: ");
				for(String a : taContent.invisible)
					System.out.println("  " + a);
				break;
			case Signature:
				Signature sig = (Signature)info;
				String desc = sig.getSignature();
				String parsedDesc = parse(desc, true);
				String genericsType
					= parsedDesc.substring(parsedDesc.indexOf("<") + 1, parsedDesc.lastIndexOf(">"));
				for(String type : genericsType.split(",")) {
					String[] typeAndExtends = type.split(" extends ");
					if(typeAndExtends.length > 1) {
						genericsMap.put(typeAndExtends[0], typeAndExtends[1]);
					} else {
						genericsMap.put(typeAndExtends[0], "java.lang.Object");
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
	 * returns annotations content.
	 * @return annotation content
	 */
	public AnnotationContent getAnnotation() {
		return annContent;
	}

	/**
	 * returns type annotations content.
	 * @return type annotation content
	 */
	public TypeAnnotationContent getTypeAnnotation() {
		return taContent;
	}

	// <editor-fold defaultstate="collapsed" desc="[class] AnnotationContent">
	/**
	 * This class is for annotations of class and member.
	 */
	public class AnnotationContent {
		/**
		 * hex of visible.
		 */
		public static final int VISIBLE   = 0x01;
		/**
		 * hex of invisible.
		 */
		public static final int INVISIBLE = 0x10;
		int count;
		int type;
		String[] visible;
		String[] invisible;

		AnnotationContent(String[] annotations, boolean isVisible) {
			this(isVisible);
			initAnnotation(annotations, isVisible);
		}

		AnnotationContent(boolean isVisible) {
			hasAnnotation = true;
			type = isVisible ? VISIBLE : INVISIBLE;
		}

		void setInvisible(String[] annotations) {
			type = type | INVISIBLE;
			initAnnotation(annotations, false);
		}

		void initAnnotation(String[] annotations, boolean isVisible) {
			this.count = annotations.length;
			if(isVisible) visible   = annotations;
			else          invisible = annotations;
		}

		/**
		 * returns annotations.
		 * @param isVisible whether runtime visible annotation
		 * @return annotations
		*/
		public String[] getAnnotations(boolean isVisible) {
			return isVisible ? (visible   != null ? visible   : new String[0])
							 : (invisible != null ? invisible : new String[0]);
		}

		/**
		 * returns annotation of specified array index.
		 * @param index array index
		 * @param isVisible whether runtime visible annotation
		 * @return annotation
		 */
		public String getAnnotation(int index, boolean isVisible) {
			if(0 <= index && index <= count) {
				return isVisible ? visible[index] : invisible[index];
			}
			throw new ArrayIndexOutOfBoundsException(index);
		}

		/**
		 * return whether specified type matches this content type.<br>
		 * ex). <code>checkType(AnnotationContent.VISIBLE);</code>
		 * @param type hex of visible or invisible
		 * @return if specified type matches this content type, this method returns true.<br>
		 * Otherwise, it returns false.
		 */
		public boolean checkType(int type) {
			return (type | this.type) == this.type;
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="[class] TypeAnnotationContent">
	/**
	 * This class is for type annotations of class and member.
	 */
	public class TypeAnnotationContent extends AnnotationContent {
		String[] targets; 
		String[] invTargets;

		TypeAnnotationContent(String[] ta, boolean isVisible) {
			super(ta, isVisible);
			if(isVisible) {
				this.targets = ta;
//				for(int i = 0; i < count; i++) {
//					initTarget(ta[i].getTargetInfo(), i, isVisible);
//				}
			} else {
				setInvisible(ta);
			}
		}

		void initTarget(TargetInfo target, int annIndex, boolean isVisible) {}

		@Override
		void setInvisible(String[] annotations) {
			super.setInvisible(annotations);
			this.invTargets = new String[annotations.length];
//			TypeAnnotation[] ta = (TypeAnnotation[])annotations;
//			for(int i = 0; i < ta.length; i++) {
//				initTarget(ta[i].getTargetInfo(), i, false);
//			}
		}

		/**
		 * returns target info of annotation.
		 * @param isVisible whether runtime visible annotation
		 * @return target info
		 */
		public String[] getTargets(boolean isVisible) {
			return isVisible ? (targets    != null ? targets    : new String[0])
							 : (invTargets != null ? invTargets : new String[0]);
		}
	}
	// </editor-fold>
}