package sds.classfile;

import java.util.Arrays;
import java.util.Iterator;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for constant-pool of classfile.
 * @author inagaki
 */
public class ConstantPool extends AbstractArrayInfo<ConstantInfo> implements Iterable<ConstantInfo> {
	/**
	 * constructor.
	 * @param size constant-pool size
	 */
	public ConstantPool(int size) {
		this.elements = new ConstantInfo[size];
	}

	/**
	 * returns element of specified index in constant-pool.
	 * @param index array index
	 * @return constant-pool element
	 */
	public ConstantInfo get(int index) {
		return (ConstantInfo)elements[index];
	}

	@Override
	public Iterator<ConstantInfo> iterator() {
		return Arrays.asList((ConstantInfo[])elements).iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = System.getProperty("line.separator");
		sb.append("*** Constant Pool ***").append(sep);
		for(int i = 0; i < elements.length; i++) {
			sb.append("[").append(i+1).append("]: ").append(elements[i]).append(sep);
		}
		return sb.toString();
	}
}