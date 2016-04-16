package sds.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.6">
 * InnerClasses Attribute</a>.
 * @author inagaki
 */
public class InnerClasses extends AttributeInfo {
	private Classes[] classes;

	/**
	 * constructor.
	 * @param nameIndex constant-pool entry index of attribute name
	 * @param length attribute length
	 */
	public InnerClasses(int nameIndex, int length) {
		super(AttributeType.InnerClasses, nameIndex, length);
	}

	/**
	 * returns inner classes.
	 * @return classes
	 */
	public Classes[] getClasses() {
		return classes;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		this.classes = new Classes[raf.readShort()];
		for(int i = 0; i < classes.length; i++) {
			classes[i] = new Classes(raf);
		}
	}

	/**
	 * This class is for inner class.
	 */
	public class Classes {
		private int innerClassInfoIndex;
		private int outerClassInfoIndex;
		private int innerNameIndex;
		private int innerClassAccessFlags;

		Classes(RandomAccessFile raf) throws IOException {
			this.innerClassInfoIndex = raf.readShort();
			this.outerClassInfoIndex = raf.readShort();
			this.innerNameIndex = raf.readShort();
			this.innerClassAccessFlags = raf.readShort();
		}

		/**
		 * returns constant-pool entry index.<br><br>
		 * if key is "inner", it returns constant-pool entry
		 * index of inner class.<br>
		 * if key is "outer", it returns constant-pool entry
		 * index of class has this inner class.<br>
		 * if key is "inner_name", it returns constant-pool
		 * entry index of inner class name.<br>
		 * if key is "access_flag", it returns access flag 
		 * of inner class.<br>
		 * by default, it returns -1.
		 * @param key value name
		 * @return constant-pool entry index
		 */
		public int getNumber(String key) {
			switch(key) {
				case "inner":       return innerClassInfoIndex;
				case "outer":       return outerClassInfoIndex;
				case "inner_name":  return innerNameIndex;
				case "access_flag": return innerClassAccessFlags;
				default:
					System.out.println("Invalid key in InnerClasses: " + key);
					return -1;
			}
		}
	}
}