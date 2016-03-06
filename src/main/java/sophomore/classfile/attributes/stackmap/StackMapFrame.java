package sophomore.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
class StackMapFrame {
	int sameFrame;
	int sameLocalsStackItemFrame;
	int sameLocalsStackItemFrameExtended;
	int chopFrame;
	int sameFramExtended;
	int appendFrame;
	int fullFrame;
	StackMapFrame(RandomAccessFile raf) throws IOException {
		
	}
}
