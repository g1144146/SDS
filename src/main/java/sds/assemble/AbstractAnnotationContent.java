package sds.assemble;

import sds.classfile.ConstantPool;
import sds.classfile.attributes.annotation.Annotation;
import sds.classfile.attributes.annotation.ElementValueException;
import static sds.util.AnnotationParser.parseAnnotation;

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

	AbstractAnnotationContent(Annotation[] annotations, ConstantPool pool) {
		try {
			for(int i = 0; i < annotations.length; i++) {
				this.annotations[i] = parseAnnotation(annotations[i], new StringBuilder(), pool);
			}
		} catch(ElementValueException e) {
			e.printStackTrace();
		}
	}

	abstract void setInvisible(Annotation[] annotations, ConstantPool pool);
}