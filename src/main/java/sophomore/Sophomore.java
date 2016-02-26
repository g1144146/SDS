package sophomore;

public class Sophomore {
	private String[] args;
	
	public Sophomore(String[] args) {
		this.args = args;
	}
	
	public void run() {
		for(String arg : args) {
			ClassFileReader reader = new ClassFileReader(arg);
			
		}
	}
}