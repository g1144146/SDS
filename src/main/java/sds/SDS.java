package sds;

import sds.assemble.ClassContent;

/**
 * This class is for driving SDS.
 * @author inagaki
 */
public class SDS {
	private String[] args;

	/**
	 * constructor.
	 * @param args command line arguments
	 */
	public SDS(String[] args) {
		this.args = args;
	}

	/**
	 * starts SDS.
	 */
	public void run() {
		for(String arg : args) {
			ClassFileReader reader = new ClassFileReader(arg);
			reader.read();
//			ClassContent cc = new ClassContent(reader.getClassFile());
		}
	}
}