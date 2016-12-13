package sds.decompile;

import java.util.NoSuchElementException;

/**
 * This adapter class is for
 * {@link LocalStack <code>LocalStack</code>}
 * and
 * {@link OperandStack <code>OperandStack</code>}.
 */
public abstract class SimpleStack {
	String[] stack;
	int current;

	/**
	 * constructor.
	 * @param size max size of stack
	 */
	public SimpleStack(int size) {
		this.stack = new String[size];
		this.current = 0;
	}

	/**
	 * returns this stack's current size.
	 * @return current size
	 */
	public int getCurrentStackSize() {
		return current;
	}

	/**
	 * push element to stack.
	 * @param element
	 */
	public void push(String element) {
		if(element == null) {
			throw new IllegalArgumentException("argument is null.");
		}
		if(current >= stack.length) {
			throw new ArrayIndexOutOfBoundsException("stack size is over specified size (" + stack.length + ") .");
		}
		stack[current] = element;
		current++;
	}

	/**
	 * returns top element of stack.
	 * @return top element
	 */
	public String pop() {
		if(current > 0) {
			current--;
		}
		String element = stack[current];
		if(element == null) {
			throw new NoSuchElementException("stack is empty.");
		}
		stack[current - 1] = null;
		return element;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(current == 0) {
			for(int i = stack.length - 1; i >= 0; i--) {
				sb.append("[").append(i).append("]:").append("\n");
			}
		} else {
			for(int i = stack.length - 1; i >= 0; i--) {
				sb.append("[").append(i).append("]: ").append(stack[i]).append("\n");
			}
		}
		return sb.toString();
	}
}