package sds.classfile;

import java.util.Arrays;
import java.util.Iterator;
import sds.classfile.attributes.AttributeInfo;

/**
 * This class is for attributes of class.
 * @author inagaki
 */
public class Attributes extends AbstractArrayInfo<AttributeInfo> implements Iterable<AttributeInfo> {
	/**
	 * constructor.
	 * @param size size of array
	 */
	public Attributes(int size) {
		this.elements = new AttributeInfo[size];
	}

	@Override
	public Iterator<AttributeInfo> iterator() {
		return Arrays.asList((AttributeInfo[])elements).iterator();
	}

	@Override
	public String toString() {
		if(elements.length == 0) {
			return "this class has no attribute";
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < elements.length; i++) {
			sb.append("[").append(i).append("]: ").append(elements[i].toString()).append("\n");
		}
		return sb.toString();
	}
}