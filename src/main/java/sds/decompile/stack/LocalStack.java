package sds.decompile.stack;

/**
 * This class is for local variable stack.
 * @author inagaki
 */
public class LocalStack extends SimpleStack {
	public LocalStack() {
		super();
	}

	/**
	 * return returns variable type of specified index.
	 * @param index stack index
	 * @return variable type
	 */
	public String loadType(int index) {
		return type.get(index);
	}

	/**
	 * returns vaiable of specified index.<br>
	 * this method is for except storing opcode.
	 * @param index stack index
	 * @return variable
	 */
	public String load(int index) {
		return stack.get(index);
	}

	/**
	 * returns variable of specified index.<br>
	 * this method is for storing opcode.
	 * @param index stack index
	 * @param type variable type
	 * @return variable
	 */
	public String load(int index, String type) {
		if(index >= stack.size()) {
			int argNumber = stack.size();
			push("val_" + argNumber, type);
			if(type.matches("double|long")) {
				push("val_" + argNumber, type);
			}
		}
		return stack.get(index);
	}
}