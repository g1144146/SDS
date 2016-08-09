package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.ConstantPool;
import static sds.util.AccessFlags.get;
import static sds.util.Utf8ValueExtractor.extract;

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
	public void read(ClassFileStream data, ConstantPool pool) throws IOException {
		this.classes = new Classes[data.readShort()];
		for(int i = 0; i < classes.length; i++) {
			classes[i] = new Classes(data, pool);
		}
	}

	/**
	 * This class is for inner class.
	 */
	public class Classes {
		private String innerClass;
		private String outerClass;
		private String innerName;
		private String innerClassAccessFlags;

		Classes(ClassFileStream data, ConstantPool pool) throws IOException {
			int innerClassInfoIndex = data.readShort();
			int outerClassInfoIndex = data.readShort();
			int innerNameIndex = data.readShort();
			int accessFlags = data.readShort();
			if(checkRange(innerClassInfoIndex-1, pool.size())) {
				this.innerClass = extract(pool.get(innerClassInfoIndex-1), pool);
			}
			if(checkRange(outerClassInfoIndex-1, pool.size())) {
				this.outerClass = extract(pool.get(outerClassInfoIndex-1), pool);
			}
			if(checkRange(innerNameIndex-1, pool.size())) {
				this.innerName  = extract(pool.get(innerNameIndex-1), pool);
			}
			this.innerClassAccessFlags = get(accessFlags, "nested");
		}

		private boolean checkRange(int index, int size) {
			return (0 <= index) && (index < size);
		}

		/**
		 * returns string accord with specified key.<br><br>
		 * if key is "inner", it returns inner class.<br>
		 * if key is "outer", it returns class has this inner class.<br>
		 * if key is "inner_name", it returns inner class name.<br>
		 * if key is "access_flag", it returns access flag 
		 * of inner class.<br>
		 * by default, it returns string of length 0.
		 * @param key value name
		 * @return string
		 */
		public String getNumber(String key) {
			switch(key) {
				case "inner":       return innerClass;
				case "outer":       return outerClass;
				case "inner_name":  return innerName;
				case "access_flag": return innerClassAccessFlags;
				default:
					System.out.println("Invalid key in InnerClasses: " + key);
					return "";
			}
		}
	}
}