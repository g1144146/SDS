package sophomore;

import sophomore.classfile.Attributes;
import sophomore.classfile.Fields;
import sophomore.classfile.Methods;
import sophomore.classfile.ConstantPool;

public class ClassFile {
	/**
	 * 
	 */
	int magicNumber;
	/**
	 * 
	 */
	int majorVersion;
	/**
	 * 
	 */
	int minorVersion;
	/**
	 * 
	 */
	ConstantPool cp;
	/**
	 * 
	 */
	int accessFlag;
	/**
	 * 
	 */
	int thisClass;
	/**
	 * 
	 */
	int superClass;
	/**
	 * 
	 */
	int interfaceCount;
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
}