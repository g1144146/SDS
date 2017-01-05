package sds.decompile;

/**
 * This class is for local variable stack.
 * @author inagaki
 */
public class LocalStack extends SimpleStack {
	public LocalStack() {
		super();
	}

	/**
	 * returns argument of specified index.
	 * @param index array index
	 * @return argument
	 */
	public String load(int index) {
		return load(index, false);
	}

	/**
	 * returns argument of specified index.
	 * @param index array index
	 * @param isLongOrDouble  whether type of the specified local variable is long or double.
	 * @return argument
	 */
	public String load(int index, boolean isLongOrDouble) {
		if(index >= stack.size()) {
			int argNumber = stack.size();
			stack.push("arg" + argNumber);
			if(isLongOrDouble) {
				stack.push("arg" + argNumber);
			}
		}
		return stack.get(index);
	}
}