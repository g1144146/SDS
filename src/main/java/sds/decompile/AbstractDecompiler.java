package sds.decompile;

import sds.assemble.BaseContent;
import sds.assemble.BaseContent.AnnotationContent;

/**
 * This adapter class is for decompiler.
 * @author inagaki
 */
public abstract class AbstractDecompiler implements Decompiler {
	DecompiledResult result;

	/**
	 * Constructs an AbstractDecompiler with the specified result.
	 * @param result decompiled result
	 */
	AbstractDecompiler(DecompiledResult result) {
		this.result = result;
	}

	/**
	 * Adds declaration of the content to the result.
	 * @param content content to add declaration
	 */
	abstract void addDeclaration(BaseContent content);

	/**
	 * Adds annotations to the result.
	 * @param annotation annotation content
	 */
	void addAnnotation(AnnotationContent annotation) {
		if(annotation != null) {
			// runtime visible annotation
			if(annotation.getAnnotations(true).length > 0) {
				for(String ann : annotation.getAnnotations(true)) {
					result.write(ann);
				}
			}
			// runtime invisible annotation
			if(annotation.getAnnotations(false).length > 0) {
				for(String ann : annotation.getAnnotations(false)) {
					result.write(ann);
				}
			}
		}
	}

	/**
	 * retunrs decompiled source.
	 * @return decompiled source
	 */
	public DecompiledResult getResult() {
		return result;
	}

	@Override
	public void decompile(BaseContent[] contents) {
		for(BaseContent content : contents) {
			decompile(content);
		}
	}
}
