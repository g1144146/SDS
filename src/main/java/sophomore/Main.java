package sophomore;

public class Main {
	
	public Main(String[] args) {
		Sophomore sophomore = new Sophomore(args);
		sophomore.run();
	}
	
	public static void main(String[] args) {
		new Main(args);
	}
}