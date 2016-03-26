package sophomore.classfile.attributes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author inagaki
 */
public class InnerClasses extends AttributeInfo {
	/**
	 * 
	 */
	Classes[] classes;

	/**
	 * 
	 * @param nameIndex
	 * @param length 
	 */
	public InnerClasses(int nameIndex, int length) {
		super(AttributeType.InnerClasses, nameIndex, length);
	}

	/**
	 * 
	 * @return 
	 */
	public Classes[] getClasses() {
		return classes;
	}

	@Override
	public void read(RandomAccessFile raf) throws IOException {
		int len = raf.readShort();
		this.classes = new Classes[len];
		for(int i = 0; i < len; i++) {
			classes[i] = new Classes(raf);
		}
	}
	
	public class Classes {
		/**
		 * The constant_pool entry at that index must be a CONSTANT_Class_info structure.
		 */
		int innerClassInfoIndex;
		/**
		 * the entry at that index must be a CONSTANT_Class_info structure.
		 */
		int outerClassInfoIndex;
		/**
		 * the entry at that index must be a CONSTANT_Utf8_info structure.
		 */
		int innerNameIndex;
		/**
		 *
		 */
		int innerClassAccessFlags;
		
		/**
		 * 
		 * @param raf
		 * @throws IOException 
		 */
		public Classes(RandomAccessFile raf) throws IOException {
			this.innerClassInfoIndex = raf.readShort();
			this.outerClassInfoIndex = raf.readShort();
			this.innerNameIndex = raf.readShort();
			this.innerClassAccessFlags = raf.readShort();
		}

		/**
		 * 
		 * @param key
		 * @return 
		 */
		public int getNumber(String key) {
			switch(key) {
				case "inner":
					return innerClassInfoIndex;
				case "outer":
					return outerClassInfoIndex;
				case "inner_name":
					return innerNameIndex;
				case "access_flag":
					return innerClassAccessFlags;
				default:
					System.out.println("Invalid key in InnerClasses: " + key);
					return -10000;
			}
		}
	}
}
