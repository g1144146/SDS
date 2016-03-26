package sophomore.classfile.constantpool;

public class ConstantTypeException extends Exception {
	public ConstantTypeException() {
		super();
	}

	public ConstantTypeException(int tag) {
		super("Tag " + tag + " is unknown.");
	}
}