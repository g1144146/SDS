package sds.classfile;

import java.io.IOException;

/**
 * This interface is for info which classfile has.
 * @author inagaki
 */
public interface Info {
	/**
	 * reads info from classfile.
	 * @param data classfile stream
	 * @param pool constant-pool
	 * @throws IOException 
	 */
	abstract void read(ClassFileStream data, ConstantPool pool) throws Exception;
}