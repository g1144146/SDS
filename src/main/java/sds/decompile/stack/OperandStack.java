package sds.decompile.stack;

/**
 * This class is for operand stack.
 * @author inagaki
 */
public class OperandStack extends SimpleStack {
    /**
     * constructor.
     */
    public OperandStack() {
        super();
    }

    /**
     * push int value element to stack.
     * @param element int value
     */
    public void push(int element) {
        push(Integer.toString(element), "int");
    }

    /**
     * push long value element to stack.
     * @param element long value
     */
    public void push(long element) {
        push(Long.toString(element) + "L", "long");
    }

    /**
     * push float value element to stack.
     * @param element float value
     */
    public void push(float element) {
        push(Float.toString(element) + "f", "float");
    }

    /**
     * push double value element to stack.
     * @param element double value
     */
    public void push(double element) {
        push(Double.toString(element) + "d", "double");
    }
}