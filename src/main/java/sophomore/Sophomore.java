package sophomore;

/**
 * 
 * @author inagaki
 */
public class Sophomore {
	/**
	 * 
	 */
	private String[] args;

	/**
	 * 
	 * @param args 
	 */
	public Sophomore(String[] args) {
		this.args = args;
	}

	/**
	 * 
	 */
	public void run() {
		for(String arg : args) {
			ClassFileReader reader = new ClassFileReader(arg);
			reader.read();
		}
	}
}