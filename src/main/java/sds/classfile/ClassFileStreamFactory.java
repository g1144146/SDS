package sds.classfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * This factory class is for {@link ClassFileStream <code>ClassFileStream</code>}.
 * @author inagaki
 */
public class ClassFileStreamFactory {
    /**
     * returns created classfile stream from file path.
     * @param filePath file path
     * @return classfile stream
     * @throws IOException 
     */
    public ClassFileStream create(String filePath) throws IOException {
        return new WithRandomAccessFile(filePath);
    }

    /**
     * returns created classfile stream from file stream.
     * @param stream file stream
     * @return classfile stream
     * @throws IOException 
     */
    public ClassFileStream create(InputStream stream) throws IOException {
        return new WithDataInputStream(stream);
    }
}