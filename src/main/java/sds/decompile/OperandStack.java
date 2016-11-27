package sds.decompile;

/**
 * This class is for operand stack.
 * @author inagaki
 */
public class OperandStack extends SimpleStack {
	/**
	 * constructor.
	 * @param size max size of stack
	 */
	public OperandStack(int size) {
		super(size);
	}

	/**
	 * push int value element to stack.
	 * @param element int value
	 */
	public void push(int element) {
		push(Integer.toString(element));
	}

	/**
	 * push long value element to stack.
	 * @param element long value
	 */
	public void push(long element) {
		push(Long.toString(element) + "L");
	}

	/**
	 * push float value element to stack.
	 * @param element float value
	 */
	public void push(float element) {
		push(Float.toString(element) + "f");
	}

	/**
	 * push double value element to stack.
	 * @param element double value
	 */
	public void push(double element) {
		push(Double.toString(element) + "d");
	}
}