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

	public void investigateAttribute(AttributeInfo info, ConstantPool pool) {
		switch(info.getType()) {
			case Deprecated: break;
			case RuntimeVisibleAnnotations:
				RuntimeVisibleAnnotations rva = (RuntimeVisibleAnnotations)info;
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
//				for(TypeAnnotation pa : rvta.getAnnotations()) {
//					printTargetInfo(pa.getTargetInfo());
//					printTypePath(pa.getTargetPath());
//					for(ElementValuePair e : pa.getElementValuePairs()) {
//						printElementValuePair(e);
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
			case Signature:
				Signature sig = (Signature)info;
				String signature = extract(pool.get(sig.getSignatureIndex()-1), pool);
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
			case Synthetic: break;
			default:        break;
		}
	}

	/**
	 * return generics map.
	 * @return map
	 */
	public Map<String, String> geGenericsMap() {
		return genericsMap;
	}
}
