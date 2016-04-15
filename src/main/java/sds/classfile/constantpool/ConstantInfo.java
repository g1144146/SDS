package sds.classfile.constantpool;

import sds.classfile.Info;

/**
 * This adapter class is for info on constant-pool.
 * @author inagaki
 */
public abstract class ConstantInfo implements Info {
	private int tag;

	/**
	 * constructor.
	 * @param tag constant info tag
	 */
	public ConstantInfo(int tag) {
		this.tag = tag;
	}

	/**
	 * returns constant info tag.
	 * @return constant info tag
	 */
	public int getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return ConstantType.get(tag);
	}
}