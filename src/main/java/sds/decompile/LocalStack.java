package sds.decompile;

/**
 * This class is for local variable stack.
 * @author inagaki
 */
public class LocalStack extends SimpleStack {
	public LocalStack(int size) {
		super(size);
	}

	/**
	 * returns argument of specified index.
	 * @param index array index
	 * @return argument
	 */
	public String load(int index) {
		if(index >= stack.length) {
			throw new ArrayIndexOutOfBoundsException("stack size is over specified size (" + stack.length + ") .");
		}
		return stack[index];
	}
}