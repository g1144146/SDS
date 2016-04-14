package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for annotations table.<br>
 * {@link RuntimeVisibleTypeAnnotations <code>RuntimeVisibleTypeAnnotations</code>} and 
 * {@link RuntimeInvisibleTypeAnnotations <code>RuntimeInvisibleTypeAnnotations</code>} have item.
 * @author inagaki
 */
public class TypeAnnotation extends Annotation {
	/**
	 * type in a declaration or expression is annotated.
	 */
	TargetInfo targetInfo;
	/**
	 * part of the type indicated by target_info is annotated.
	 */
	TypePath targetPath;

	/**
	 * constructor
	 * @param raf classfile stream
	 * @throws IOException
	 */
	TypeAnnotation(RandomAccessFile raf)
	throws IOException, TargetTypeException, ElementValueException {
		TargetInfoBuilder builder = TargetInfoBuilder.getInstance();
		this.targetInfo = builder.build(raf);
		this.targetPath = new TypePath(raf);
		this.typeIndex = raf.readShort();
		this.elementValuePairs = new ElementValuePair[raf.readShort()];
		for(int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(raf);
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