package sds.classfile.attributes.annotation;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class TypeAnnotation extends Annotation {
	/**
	 * 
	 */
	TargetInfo targetInfo;
	/**
	 * 
	 */
	TypePath targetPath;

	/**
	 * 
	 * @param raf
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

	public TargetInfo getTargetInfo() {
		return targetInfo;
	}

	public TypePath getTargetPath() {
		return targetPath;
	}
}