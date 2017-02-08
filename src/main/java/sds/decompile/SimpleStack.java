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
	 * pushes element and type onto each stack.
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
		stack.push(element);
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
	 * returns top element of stack.
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
		return stack.pop();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		if(stack.size() > 0) {
			for(int i = 0; i < stack.size() - 1; i++) {
				sb.append(type.get(i)).append(": ").append(stack.get(i)).append(", ");
			}
			sb.append(type.get(type.size() - 1)).append(": ").append(stack.get(stack.size() - 1));
		}
		return sb.append("]").toString();
	}
}