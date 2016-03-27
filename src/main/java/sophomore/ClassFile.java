package sophomore;

import sophomore.classfile.AccessFlags;
import sophomore.classfile.Attributes;
import sophomore.classfile.ConstantPool;
import sophomore.classfile.Fields;
import sophomore.classfile.Methods;

/**
 * 
 * @author inagaki
 */
public class ClassFile {
	/**
	 * 
	 */
	int magicNumber = -100;
	/**
	 * 
	 */
	int majorVersion = -100;
	/**
	 * 
	 */
	int minorVersion = -100;
	/**
	 * 
	 */
	ConstantPool pool;
	/**
	 * 
	 */
	int accessFlag = -100;
	/**
	 * 
	 */
	int thisClass = -100;
	/**
	 * 
	 */
	int superClass = -100;
	/**
	 * 
	 */
	int[] interfaces;
	/**
	 * 
	 */
	Fields fields;
	/**
	 * 
	 */
	Methods methods;
	/**
	 * 
	 */
	Attributes attr;

//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		if(magicNumber  != -100) sb.append(  "Magic Number : ").append(magicNumber);
//		if(majorVersion != -100) sb.append("\nMajor Version: ").append(majorVersion);
//		if(minorVersion != -100) sb.append("\nMinor Version: ").append(minorVersion);
//		if(pool         != null) sb.append("\n").append(pool.toString());
//		if(accessFlag   != -100) sb.append("\nAccess Flag  : ").append(AccessFlags.get("class", accessFlag));
//		if(thisClass    != -100) sb.append("\nThis Class   : ").append("");
//
//		return sb.toString();
//	}
}