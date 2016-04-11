package sds;

import sds.classfile.Attributes;
import sds.classfile.ConstantPool;
import sds.classfile.Fields;
import sds.classfile.Methods;

/**
 * 
 * @author inagaki
 */
public class ClassFile {
	/**
	 * 
	 */
	int magicNumber = -1;
	/**
	 * 
	 */
	int majorVersion = -1;
	/**
	 * 
	 */
	int minorVersion = -1;
	/**
	 * 
	 */
	ConstantPool pool;
	/**
	 * 
	 */
	int accessFlag = -1;
	/**
	 * 
	 */
	int thisClass = -1;
	/**
	 * 
	 */
	int superClass = -1;
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

	public int getMagic()      { return magicNumber;  }
	public int getMajor()      { return majorVersion; }
	public int getMinor()      { return minorVersion; }
	public int getAccessFlag() { return accessFlag;   }
	public int getThisClass()  { return thisClass;    }
	public int getSuperClass() { return superClass;   }
	public int[] getInterfaces()  { return interfaces; }
	public ConstantPool getPool() { return pool;       }
	public Fields getFields()     { return fields;     }
	public Methods getMethods()   { return methods;    }
	public Attributes getAttr()   { return attr;       }
}