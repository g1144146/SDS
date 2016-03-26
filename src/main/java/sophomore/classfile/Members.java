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
	public int getSize() {
		return elements.length;
	}

	@Override
	public void add(int index, MemberInfo element) {
		if(index >= elements.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		elements[index] = element;
	}

	@Override
	public MemberInfo get(int index) {
		if(index >= elements.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return elements[index];
	}

	@Override
	public MemberInfo[] getAll() {
		return elements;
	}
}