package sds.classfile;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for stream of read classfile.
 * @author inagaki
 */
public class ClassFileStream implements AutoCloseable {
    private DataInputStream stream;
    private RandomAccessFile raf;
    private long filePointer;

    /**
     * constructor.
     * @param raf classfile stream with {@code RandomAccessFile}
     */
    public ClassFileStream(RandomAccessFile raf) {
        this.raf = raf;
    }

    /**
     * constructor.
     * @param in
     * @throws java.io.IOException
     */
    public ClassFileStream(InputStream in) throws IOException {
        this.stream = new DataInputStream(in);
    }

    /**
     * returns current file pointer.
     * @return file pointer
     * @throws IOException
     */
    public long getFilePointer() throws IOException {
        return (raf != null) ? raf.getFilePointer() : filePointer;
    }

    /**
     * returns signed eight-bit value.
     * @return signed eight-bit value
     * @throws IOException
     */
    public byte readByte() throws IOException {
        return isRaf(Byte.BYTES) ? raf.readByte() : stream.readByte();
    }

    /**
     * returns unsigned eight-bit value.
     * @return unsigned eight-bit value
     * @throws IOException
     */
    public int readUnsignedByte() throws IOException {
        return isRaf(Byte.BYTES) ? raf.readUnsignedByte() : stream.readUnsignedByte();
    }

    /**
     * returns character.
     * @return character
     * @throws IOException
     */
    public char readChar() throws IOException {
        return isRaf(Character.BYTES) ? raf.readChar() : stream.readChar();
    }

    /**
     * retunrs double.
     * @return double
     * @throws IOException
     */
    public double readDouble() throws IOException {
        return isRaf(Double.BYTES) ? raf.readDouble() : stream.readDouble();
    }

    /**
     * returns float.
     * @return float
     * @throws IOException
     */
    public float readFloat() throws IOException {
        return isRaf(Float.BYTES) ? raf.readFloat() : stream.readFloat();
    }

    /**
     * returns signed 4-bit value.
     * @return signed 4-bit value
     * @throws IOException
     */
    public int readInt() throws IOException {
        return isRaf(Integer.BYTES) ? raf.readInt(): stream.readInt();
    }

    /**
     * retunrs signed 8-bit value.
     *
     * @return signed 8-bit value
     * @throws IOException
     */
    public long readLong() throws IOException {
        return isRaf(Long.BYTES) ? raf.readLong() : stream.readLong();
    }

    /**
     * returns signed 2-bit value.
     * @return signed 2-bit value
     * @throws IOException
     */
    public short readShort() throws IOException {
        return isRaf(Short.BYTES) ? raf.readShort() : stream.readShort();
    }

    /**
     * returns unsigned 2-bit value.
     * @return unsigned 2-bit value
     * @throws IOException
     */
    public int readUnsignedShort() throws IOException {
        return isRaf(Short.BYTES) ? raf.readUnsignedShort() : stream.readUnsignedShort();
    }

    /**
     * reads specified byte array length from classfile stream, and returns that.
     * @param b byte array
     * @return byte array
     * @throws IOException
     */
    public byte[] readFully(byte[] b) throws IOException {
        if(isRaf(b.length)) {
            raf.readFully(b);
            return b;
        }
        stream.readFully(b);
        return b;
    }

    /**
     * skips over specified bytes.
     * @param n specified bytes
     * @throws IOException
     */
    public void skipBytes(int n) throws IOException {
        if(isRaf(n)) {
            raf.skipBytes(n);
            return;
        }
        stream.skipBytes(n);
    }

    private boolean isRaf(int pointer) {
        filePointer += pointer;
        return raf != null;
    }

    @Override
    public void close() throws IOException {
        if((raf != null)) {
            raf.close();
            return;
        }
        stream.close();
    }
}