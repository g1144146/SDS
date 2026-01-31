package sds;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import sds.assemble.ClassContent;
import sds.decompile.DecompileProcessor;
import sds.util.ClassFilePrinter;

/**
 * This class is for driving SDS.
 * @author inagaki
 */
public class SDS {
	private List<String> classFiles;
	private JarFile jar;

	/**
	 * constructor.
	 * @param args command line arguments
	 */
	public SDS(String[] args) {
		this.classFiles = new ArrayList<>();
		for(String arg : args) {
			parseArg(arg);
		}
	}

	/**
	 * starts SDS.
	 */
	public void run() {
		// classfile
		if(classFiles.size() > 0) {
			analyzeClassFile();
		}
		// jar
		if(jar != null) {
			analyzeJarFile();
		}
	}

	/**
	 * Parses a command line argument and adds it to the appropriate list.
	 * @param arg command line argument to parse
	 */
	private void parseArg(String arg) {
		if(arg.endsWith(".class")) {
			classFiles.add(arg);
		} else if(arg.endsWith(".jar")) {
			try {
				this.jar = new JarFile(new File(arg));
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(arg + " is not classfile or jar.");
		}
	}

	/**
	 * Analyzes class files and performs decompilation.
	 */
	private void analyzeClassFile() {
		boolean output = true;
		for(String arg : classFiles) {
			ClassFileReader reader = new ClassFileReader(arg);
			reader.read();
			ClassFile cf = reader.getClassFile();
			printClassFile(cf, output);
			ClassContent cc = new ClassContent(cf);
			DecompileProcessor dp = new DecompileProcessor();
			dp.process(cc);
		}
	}

	/**
	 * Analyzes JAR file entries and processes each class file.
	 */
	private void analyzeJarFile() {
		boolean output = true;
		try {
			for(Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				String fileName = entry.toString();
				if(! fileName.endsWith(".class")) {
					continue;
				}
				ClassFileReader reader = new ClassFileReader(jar.getInputStream(entry));
				reader.read();
				ClassFile cf = reader.getClassFile();
				printClassFile(cf, output);
				ClassContent cc = new ClassContent(cf);
			}
		} catch(Exception e) {}
	}

	/**
	 * Prints class file information for debugging purposes.
	 * @param cf class file to print
	 * @param output if true, skip printing
	 */
	private void printClassFile(ClassFile cf, boolean output) {
		if(output) {
			return;
		}
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
}
