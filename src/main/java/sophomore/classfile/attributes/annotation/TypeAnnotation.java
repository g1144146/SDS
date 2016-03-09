package sophomore.classfile.attributes.annotation;

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
	int targetType;
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
	TypeAnnotation(RandomAccessFile raf) throws IOException {
		this.targetType = raf.readByte();
		// 途中
		this.targetInfo = new TargetInfo(raf);
		this.targetPath = new TypePath(raf);
		this.typeIndex = raf.readShort();
		int len = raf.readShort();
		this.elementValuePairs = new ElementValuePair[len];
		for(int i = 0; i < len; i++) {
			elementValuePairs[i] = new ElementValuePair(raf);
		}
	}

	public int getTargetType() {
		return targetType;
	}

	public TargetInfo getTargetInfo() {
		return targetInfo;
	}

	public TypePath getTargetPath() {
		return targetPath;
	}
}