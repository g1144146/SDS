package sds.classfile;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This interface is for stream of read classfile.
 * @author inagaki
 */
public interface ClassFileStream extends AutoCloseable {
    /**
     * returns current file pointer.
     * @return file pointer
     * @throws IOException
     */
    abstract long getFilePointer() throws IOException;

    /**
     * returns signed eight-bit value.
     * @return signed eight-bit value
     * @throws IOException
     */
    abstract byte readByte() throws IOException;

    /**
     * returns unsigned eight-bit value.
     * @return unsigned eight-bit value
     * @throws IOException
     */
    abstract int readUnsignedByte() throws IOException;

    /**
     * retunrs double.
     * @return double
     * @throws IOException
     */
    abstract double readDouble() throws IOException;

    /**
     * returns float.
     * @return float
     * @throws IOException
     */
    abstract float readFloat() throws IOException;

    /**
     * returns signed 4-bit value.
     * @return signed 4-bit value
     * @throws IOException
     */
    abstract int readInt() throws IOException;

    /**
     * retunrs signed 8-bit value.
     * @return signed 8-bit value
     * @throws IOException
     */
    abstract long readLong() throws IOException;

    /**
     * returns signed 2-bit value.
     * @return signed 2-bit value
     * @throws IOException
     */
    abstract short readShort() throws IOException;

    /**
     * reads specified byte array length from classfile stream, and returns that.
     * @param b byte array
     * @return byte array
     * @throws IOException
     */
    abstract byte[] readFully(byte[] b) throws IOException;

    /**
     * skips over specified bytes.
     * @param n specified bytes
     * @throws IOException
     */
    abstract void skipBytes(int n) throws IOException;
}

class WithRandomAccessFile implements ClassFileStream {
    private final RandomAccessFile raf;

    WithRandomAccessFile(String fileName) throws IOException {
        this.raf = new RandomAccessFile(fileName, "r");
    }
    @Override public long   getFilePointer()    throws IOException { return raf.getFilePointer();   }
    @Override public byte   readByte()          throws IOException { return raf.readByte();         }
    @Override public int    readUnsignedByte()  throws IOException { return raf.readUnsignedByte(); }
    @Override public double readDouble()        throws IOException { return raf.readDouble();       }
    @Override public long   readLong()          throws IOException { return raf.readLong();         }
    @Override public int    readInt()           throws IOException { return raf.readInt();          }
    @Override public float  readFloat()         throws IOException { return raf.readFloat();        }
    @Override public short  readShort()         throws IOException { return raf.readShort();        }
    @Override public byte[] readFully(byte[] b) throws IOException { raf.readFully(b); return b;    }
    @Override public void   skipBytes(int n)    throws IOException { raf.skipBytes(n);              }
    @Override public void   close()             throws IOException { raf.close();                   }
}

class WithDataInputStream implements ClassFileStream {
    private final DataInputStream stream;
    private long pointer = 0L;

    WithDataInputStream(InputStream input) throws IOException {
        this.stream = new DataInputStream(input);
    }
    @Override public byte readByte()        throws IOException { return read(Byte.BYTES,    stream.readByte());         }
    @Override public int readUnsignedByte() throws IOException { return read(Byte.BYTES,    stream.readUnsignedByte()); }
    @Override public double readDouble()    throws IOException { return read(Double.BYTES,  stream.readDouble());       }
    @Override public long readLong()        throws IOException { return read(Long.BYTES,    stream.readLong());         }
    @Override public int readInt()          throws IOException { return read(Integer.BYTES, stream.readInt());          }
    @Override public float readFloat()      throws IOException { return read(Float.BYTES,   stream.readFloat());        }
    @Override public short readShort()      throws IOException { return read(Short.BYTES,   stream.readShort());        }
    @Override public byte[] readFully(byte[] b) throws IOException { stream.readFully(b); return read(b.length, b);     }
    @Override public long getFilePointer()      throws IOException { return pointer;                                    }
    @Override public void skipBytes(int n)      throws IOException { pointer += n; stream.skipBytes(n);                 }
    @Override public void close()               throws IOException { stream.close();                                    }
    private <T> T read(long bytes, T readValue) { pointer += bytes; return readValue; }
}