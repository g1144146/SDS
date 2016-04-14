package sds.classfile;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This interface is for info which classfile has.
 * @author inagaki
 */
public interface Info {
	/**
	 * reads info from classfile.
	 * @param raf classfile stream
	 * @throws IOException 
	 */
	abstract void read(RandomAccessFile raf) throws Exception;
}
