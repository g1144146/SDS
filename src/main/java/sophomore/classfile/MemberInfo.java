package sophomore.classfile;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.attributes.AttributeInfo;
import sophomore.classfile.attributes.AttributeInfoBuilder;

/**
 *
 * @author inagaki
 */
abstract class MemberInfo implements Info {
	/**
	 * 
	 */
	int accessFlags;
	/**
	 * 
	 */
	int nameIndex;
	/**
	 * 
	 */
	int descriptorIndex;
	/**
	 * 
	 */
	Attributes attr;
	/**
	 * 
	 */
	private String type;

	/**
	 * 
	 * @param type 
	 */
	MemberInfo(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return 
	 */
	public int getAccessFlags() {
		return accessFlags;
	}

	/**
	 * 
	 * @return 
	 */
	public int getNameIndex() {
		return nameIndex;
	}

	/**
	 * 
	 * @return 
	 */
	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	/**
	 * 
	 * @return 
	 */
	public Attributes getAttr() {
		return attr;
	}

	/**
	 * 
	 * @return 
	 */
	public String getType() {
		return type;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.accessFlags = raf.readShort();
		this.nameIndex = raf.readShort();
		this.descriptorIndex = raf.readShort();
		int attrCount = raf.readShort();
		this.attr = new Attributes(attrCount);
		for(int i = 0; i < attrCount; i++) {
			AttributeInfoBuilder builder = AttributeInfoBuilder.getInstance();
			AttributeInfo info;
		}
//		attr.(raf);
	}
}
