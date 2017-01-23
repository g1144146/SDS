package sds.decompile;

import java.util.Stack;

/**
 * This adapter class is for
 * {@link LocalStack <code>LocalStack</code>}
 * and
 * {@link OperandStack <code>OperandStack</code>}.
 */
public abstract class SimpleStack {
	Stack<String> stack;
	Stack<String> type;

	/**
	 * constructor.
	 */
	public SimpleStack() {
		this.stack = new Stack<>();
		this.type = new Stack<>();
	}

	/**
	 * push element and type onto each stack.
	 * @param element
	 * @param type 
	 */
	public void push(String element, String type) {
		if(element == null) {
			throw new IllegalArgumentException("argument is null.");
		}
		if(type == null) {
			throw new IllegalArgumentException("type is null");
		}
		if(element.length() == 0) {
			stack.push("\"\"");
		} else {
			stack.push(element);
		}
		this.type.push(type);
	}

	/**
	 * returns top element of type stack.
	 * @return top element
	 */
	public String popType() {
		if(type.empty()) {
			throw new IllegalStateException("type stack is empty.");
		}
		return type.pop();
	}

	/**
	 * return top element of stack.
	 * @param popType whether pop type stack
	 * @return top element
	 */
	public String pop(boolean popType) {
		if(popType) {
			type.pop();
		}
		return pop();
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
		return element;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("stack: ").append(stack).append("\n")
			.append("type : ").append(type);
		return sb.toString();
	}
}