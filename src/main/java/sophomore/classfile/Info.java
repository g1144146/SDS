package sophomore.classfile;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public interface Info {
	/**
	 * 
	 * @param raf
	 * @throws IOException 
	 */
	abstract void read(RandomAccessFile raf) throws IOException;
}
