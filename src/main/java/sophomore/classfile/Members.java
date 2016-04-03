package sophomore.classfile;

/**
 * 
 * @author inagakikenichi
 */
public class Members implements ArrayInfo<MemberInfo> {
	/**
	 * 
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