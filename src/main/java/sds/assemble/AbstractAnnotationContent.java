package sds.assemble;

/**
 * This adapter class is for
 * {@link BaseContent.AnnotationContent <code>AnnotationContent</code>}
 * ,
 * {@link BaseContent.TypeAnnotationContent <code>TypeAnnotationContent</code>}
 * and
 * {@link MethodContent.ParamAnnotationContent <code>ParamAnnotationContent</code>}.
 * @author inagaki
 */
public abstract class AbstractAnnotationContent {
	String[] annotations;

	/**
	 * returns annotations.
	 * @return annotations
	 */
	public String[] getAnnotations() {
		return annotations;
	}

	/**
	 * returns annotation of specified array index.
	 * @param index array index
	 * @return annotation
	 */
	public String getAnnotation(int index) {
		if(0 <= index && index <= annotations.length) {
			return annotations[index];
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}
}