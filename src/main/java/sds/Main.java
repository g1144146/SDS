package sds;

/**
 * Main class.
 * @author inagaki
 */
public class Main {

	/**
	 * constructor.
	 * @param args command line arguments
	 */
	public Main(String[] args) {
		SDS sophomore = new SDS(args);
		sophomore.run();
	}

	/**
	 * main method.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		new Main(args);
	}
}