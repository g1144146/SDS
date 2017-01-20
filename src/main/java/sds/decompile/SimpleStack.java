package sds.decompile;

import java.util.Stack;

/**
 * This adapter class is for
 * {@link LocalStack <code>LocalStack</code>}
 * and
 * {@link OperandStack <code>OperandStack</code>}.
 */
public abstract class SimpleStack {
	int current;
	Stack<String> stack;
	Stack<String> type;

	/**
	 * constructor.
	 */
	public SimpleStack() {
		this.stack = new Stack<>();
		this.type  = new Stack<>();
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
		if(element.length() == 0) {
			stack.push("\"\"");
		} else {
			stack.push(element);
		}
		current++;
	}

	/**
	 * returns top element of stack.
	 * @return top element
	 */
	public String pop() {
		if(stack.empty()) {
			throw new IllegalStateException("stack is empty.");
		}
		String element = stack.pop();
		current--;
		return element;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(stack);
		return sb.toString();
	}
}