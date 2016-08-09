package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeVisibleTypeAnnotations <code>RuntimeVisibleTypeAnnotations</code>} and 
 * {@link RuntimeInvisibleTypeAnnotations <code>RuntimeInvisibleTypeAnnotations</code>} have item.
 * @author inagaki
 */
public class TypeAnnotation extends Annotation {
	private TargetInfo targetInfo;
	private TypePath targetPath;

	TypeAnnotation(ClassFileStream data)
	throws IOException, TargetTypeException, ElementValueException {
		TargetInfoBuilder builder = TargetInfoBuilder.getInstance();
		this.targetInfo = builder.build(data);
		this.targetPath = new TypePath(data);
		this.typeIndex = data.readShort();
		this.elementValuePairs = new ElementValuePair[data.readShort()];
		for(int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(data);
		}
	}

	/**
	 * returns type in a declaration or expression is annotated.
	 * @return type
	 */
	public TargetInfo getTargetInfo() {
		return targetInfo;
	}

	/**
	 * returns part of the type indicated by target_info is annotated.
	 * @return part of the type
	 */
	public TypePath getTargetPath() {
		return targetPath;
	}
}