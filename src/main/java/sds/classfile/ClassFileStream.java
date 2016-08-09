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
	private boolean isRaf;

	/**
	 * constructor.
	 * @param raf classfile stream with {@code RandomAccessFile}
	 */
	public ClassFileStream(RandomAccessFile raf) {
		this.raf = raf;
		this.isRaf = true;
	}

	/**
	 * constructor.
	 * @param in
	 * @throws java.io.IOException
	 */
	public ClassFileStream(InputStream in) throws IOException {
		this.stream = new DataInputStream(in);
		this.isRaf = false;
	}	

	/**
	 * returns current file pointer.
	 * @return file pointer
	 * @throws IOException
	 */
	public long getFilePointer() throws IOException {
		return isRaf ? raf.getFilePointer() : filePointer;
	}

	/**
	 * returns signed eight-bit value.
	 * @return signed eight-bit value
	 * @throws IOException
	 */
	public byte readByte() throws IOException {
		filePointer += Byte.BYTES;
		return isRaf ? raf.readByte() : stream.readByte();
	}

	/**
	 * returns unsigned eight-bit value.
	 * @return unsigned eight-bit value
	 * @throws IOException
	 */
	public int readUnsignedByte() throws IOException {
		filePointer += Byte.BYTES;
		return isRaf ? raf.readUnsignedByte() : stream.readUnsignedByte();
	}

	/**
	 * returns character.
	 * @return character
	 * @throws IOException
	 */
	public char readChar() throws IOException {
		filePointer += Character.BYTES;
		return isRaf ? raf.readChar() : stream.readChar();
	}

	/**
	 * retunrs double.
	 * @return double
	 * @throws IOException
	 */
	public double readDouble() throws IOException {
		filePointer += Double.BYTES;
		return isRaf ? raf.readDouble() : stream.readDouble();
	}

	/**
	 * returns float.
	 * @return float
	 * @throws IOException
	 */
	public Float readFloat() throws IOException {
		filePointer += Float.BYTES;
		return isRaf ? raf.readFloat() : stream.readFloat();
	}

	/**
	 * returns signed 4-bit value.
	 * @return signed 4-bit value
	 * @throws IOException
	 */
	public int readInt() throws IOException {
		filePointer += Integer.BYTES;
		return isRaf ? raf.readInt(): stream.readInt();
	}

	/**
	 * retunrs signed 8-bit value.
	 *
	 * @return signed 8-bit value
	 * @throws IOException
	 */
	public long readLong() throws IOException {
		filePointer += Long.BYTES;
		return isRaf ? raf.readLong() : stream.readLong();
	}

	/**
	 * returns signed 2-bit value.
	 * @return signed 2-bit value
	 * @throws IOException
	 */
	public short readShort() throws IOException {
		filePointer += Short.BYTES;
		return isRaf ? raf.readShort() : stream.readShort();
	}

	/**
	 * returns unsigned 2-bit value.
	 * @return unsigned 2-bit value
	 * @throws IOException
	 */
	public int readUnsignedShort() throws IOException {
		filePointer += Short.BYTES;
		return isRaf ? raf.readUnsignedShort() : stream.readUnsignedShort();
	}

	/**
	 * reads specified byte array length from classfile stream, and returns that.
	 * @param b byte array
	 * @return byte array
	 * @throws IOException
	 */
	public byte[] readFully(byte[] b) throws IOException {
		filePointer += b.length;
		if(isRaf) {
			for(int i = 0; i < b.length; i++) {
				b[i] = raf.readByte();
			}
		} else {
			for(int i = 0; i < b.length; i++) {
				b[i] = stream.readByte();
			}
		}
		return b;
	}

	/**
	 * skips over specified bytes.
	 * @param n specified bytes
	 * @throws IOException
	 */
	public void skipBytes(int n) throws IOException {
		filePointer += n;
		if(isRaf) {
			raf.skipBytes(n);
		} else {
			for(int i = 0; i < n; i++) {
				stream.readByte();
			}
		}
	}

	@Override
	public void close() throws IOException {
		if(isRaf) {
			raf.close();
		} else {
			stream.close();
		}
	}
}