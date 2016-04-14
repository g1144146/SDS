package sds;

import sds.classfile.Attributes;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;

/**
 * This class is for classfile contents.
 * @author inagaki
 */
public class ClassFile {
	/**
	 * magic number.
	 */
	int magicNumber = -1;
	/**
	 * major version.
	 */
	int majorVersion = -1;
	/**
	 * minor version.
	 */
	int minorVersion = -1;
	/**
	 * constant-pool.
	 */
	ConstantPool pool;
	/**
	 * access flag of this class.
	 */
	int accessFlag = -1;
	/**
	 * contant-pool entry index of this class.
	 */
	int thisClass = -1;
	/**
	 * constant-pool entry index of super class.
	 */
	int superClass = -1;
	/**
	 * constant-pool entry indexes of interface.
	 */
	int[] interfaces;
	/**
	 * fields of this class.
	 */
	Fields fields;
	/**
	 * methods of this class.
	 */
	Methods methods;
	/**
	 * attributes of this class.
	 */
	Attributes attr;

	/**
	 * returns magic number.
	 * @return magic number
	 */
	public int getMagic() {
		return magicNumber;
	}
	/**
	 * returns major version
	 * @return major version
	 */
	public int getMajor() {
		return majorVersion;
	}
	/**
	 * returns minor version.
	 * @return minor version
	 */
	public int getMinor() { 
		return minorVersion;
	}
	/**
	 * returns access flag of this class.
	 * @return accsess flag
	 */
	public int getAccessFlag() {
		return accessFlag;
	}
	/**
	 * returns constant-pool entry index of this class.
	 * @return constant-pool entry index of this class
	 */
	public int getThisClass() { 
		return thisClass;
	}
	/**
	 * returns constant-pool entry index of super class.
	 * @return constant-pool entry index of super class
	 */
	public int getSuperClass() {
		return superClass;
	}
	/**
	 * returns constant-pool entry indexes of interface.
	 * @return constant-pool entry indexes of interface
	 */
	public int[] getInterfaces() { 
		return interfaces;
	}
	/**
	 * returns constant-pool.
	 * @return constant-pool
	 */
	public ConstantPool getPool() {
		return pool;
	}
	/**
	 * returns fields of this class.
	 * @return fields
	 */
	public Fields getFields() {
		return fields;
	}
	/**
	 * returns methods of this class.
	 * @return methos
	 */
	public Methods getMethods() { 
		return methods;
	}
	/**
	 * returns attributes of this class.
	 * @return attributes
	 */
	public Attributes getAttr() { 
		return attr;
	}
}