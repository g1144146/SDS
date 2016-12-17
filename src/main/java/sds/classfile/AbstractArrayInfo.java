package sds.classfile;

/**
 * This adapter class is for array of
 * {@link Attributes<code>Attributes</code>}
 * , {@link ConstantPool<code>ConstantPool</code>}
 * and {@link Members<code>Members</code>}.
 * @author inagakikenichi
 * @param <T> type extends {@link Info <code>Info</code>}
 */
public abstract class AbstractArrayInfo<T extends Info> implements ArrayInfo<Info> {
	Info[] elements;

	@Override
	public int size() {
		return elements.length;
	}

	@Override
	public void add(int index, Info element) {
		if(index >= elements.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		elements[index] = element;
	}
}