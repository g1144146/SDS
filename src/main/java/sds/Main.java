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
		SDS sds = new SDS(args);
		sds.run();
	}

	/**
	 * main method.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		new Main(args);
	}
}
