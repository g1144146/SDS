package sds.decompile;

import java.util.NoSuchElementException;

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
}