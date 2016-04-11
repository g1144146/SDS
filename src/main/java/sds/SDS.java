package sds;

/**
 * 
 * @author inagaki
 */
public class SDS {
	/**
	 * 
	 */
	private String[] args;

	/**
	 * 
	 * @param args 
	 */
	public SDS(String[] args) {
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