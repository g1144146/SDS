package sophomore.classfile.constantpool;

public class UnknownConstantTypeException extends Exception {
	public UnknownConstantTypeException() {
		super();
	}

	public UnknownConstantTypeException(int tag) {
		super("Tag " + tag + " is unknown.");
	}
}