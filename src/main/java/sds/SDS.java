package sds;

import sds.assemble.ClassContent;
import sds.decompile.DecompileProcessor;
import sds.util.ClassFilePrinter;

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
		boolean output = false;
		for(String arg : args) {
			ClassFileReader reader = new ClassFileReader(arg);
			reader.read();
			ClassFile cf = reader.getClassFile();
			if(output) {
				try {
					ClassFilePrinter printer = new ClassFilePrinter(cf.pool);
					printer.printNumber(cf.magicNumber, cf.majorVersion, cf.minorVersion);
					printer.printConstantPool();
					printer.printAccessFlag(cf.accessFlag);
					printer.printThisClass(cf.thisClass);
					printer.printSuperClass(cf.superClass);
					printer.printInterface(cf.interfaces);
					printer.printFields(cf.fields);
					printer.printMethods(cf.methods);
					printer.printAttributes(cf.attr);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			ClassContent cc = new ClassContent(cf);
			DecompileProcessor dp = new DecompileProcessor();
			dp.process(cc);
		}
	}
}