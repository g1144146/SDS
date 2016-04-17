package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.io.RandomAccessFile;

import sds.classfile.attributes.AttributeInfo;
import sds.classfile.attributes.AttributeType;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4">
 * StackMapTable Attribute</a>.
 * @author inagaki
 */
public class StackMapTable extends AttributeInfo {
	StackMapFrame[] entries;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public StackMapTable(int nameIndex, int length) {
		super(AttributeType.StackMapTable, nameIndex, length);
	}

	/**
	 * returns entries of stack-map-table.
	 * @return entries
	 */
	public StackMapFrame[] getEntries() {
		return entries;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.entries = new StackMapFrame[raf.readShort()];
		try {
			StackMapFrameBuilder builder = StackMapFrameBuilder.getInstance();
			for(int i = 0; i < entries.length; i++) {
				entries[i] = builder.build(raf);
			}
		} catch(StackMapFrameException e) {
			e.printStackTrace();
		}
	}
}