package sds.classfile.constantpool;

import sds.classfile.Info;

/**
 * 
 * @author inagaki
 */
public abstract class ConstantInfo implements Info {
	/**
	 * 
	 */
	int tag;

	/**
	 * 
	 * @param tag 
	 */
	public ConstantInfo(int tag) {
		this.tag = tag;
	}

	/**
	 * 
	 * @return 
	 */
	public int getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return "Constant Type: " + ConstantType.get(tag);
	}
}