package sds.classfile;

/**
 * This adapter class is for class has members.
 * @author inagaki
 * @param <T> type extends {@link MemberInfo <code>MemberInfo</code>}
 */
public class Members<T extends MemberInfo> implements ArrayInfo<MemberInfo> {
	/**
	 * members.
	 */
	MemberInfo[] elements;

	Members() {}

	@Override
	public int size() {
		return elements.length;
	}

	@Override
	public void add(int index, MemberInfo element) {
		if(index >= elements.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		elements[index] = element;
	}

	@Override
	public MemberInfo get(int index) {
		if(index >= elements.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return elements[index];
	}

	@Override
	public MemberInfo[] getAll() {
		return elements;
	}
}