package sds.classfile.constantpool;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
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
	public void read(ClassFileStream data, ConstantPool pool) throws Exception {
		read(data);
	}

	/**
	 * reads info from classfile.<br>
	 * method for constant info.
	 * @param data classfile stream
	 * @throws IOException 
	 */
	abstract public void read(ClassFileStream data) throws Exception;

	@Override
	public String toString() {
		return ConstantType.get(tag);
	}
}