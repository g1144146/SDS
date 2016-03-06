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
		super(AttributeType.Type.InnerClasses, nameIndex, length);
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
	
	class Classes {
		/**
		 *
		 */
		int innerClassInfoIndex;
		/**
		 *
		 */
		int outerClassInfoIndex;
		/**
		 *
		 */
		int innerNameIndex;
		/**
		 *
		 */
		int innerClassAccessFlags;
		
		/**
		 *
		 * @param icii
		 * @param ocii
		 * @param ini
		 * @param icaf
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
				case "accsess_flag":
					return innerClassAccessFlags;
				default:
					System.out.println("Invalid key: " + key);
					return -10000;
			}
		}
	}
}
