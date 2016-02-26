package sophomore;

import java.io.IOException;
import java.io.RandomAccessFile;

import sophomore.classfile.ClassFile;

public class ClassFileReader {
	private String fileName;
	private ClassFile cf;
	
	public ClassFileReader(String file) {
		this.fileName = file;
	}
	
	public void read() {
		try(RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
			
		} catch(IOException e) {
			
		}
	}

	public ClassFile getClassFile() {
		return cf;
	}
}