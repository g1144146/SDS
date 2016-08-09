package sds;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import sds.assemble.ClassContent;
import sds.decompile.DecompileProcessor;
import sds.util.ClassFilePrinter;
import sds.util.Utf8ValueExtractor;
import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * This class is for driving SDS.
 * @author inagaki
 */
public class SDS {
	private FastList<String> argList;
	private FastList<byte[]> jarContents;
	private FastList<InputStream> streams;
	private JarFile jar;

	/**
	 * constructor.
	 * @param args command line arguments
	 */
	public SDS(String[] args) {
		this.argList = new FastList<>();
		this.jarContents = new FastList<>();
		this.streams = new FastList<>();
		for(String arg : args) {
			if(arg.endsWith(".class")) {
				argList.add(arg);
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
	}

	/**
	 * starts SDS.
	 */
	public void run() {
		boolean output = true;
		// classfile
		for(String arg : argList) {
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
		
		// jar
//		for(byte[] b : jarContents) {
//		for(InputStream b : streams) {
		if(jar == null)
			return;
		try {
			for(Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				if(! entry.toString().endsWith(".class")) {
					continue;
				}
				ClassFileReader reader = new ClassFileReader(jar.getInputStream(entry));
				reader.read();
				ClassFile cf = reader.getClassFile();
				if(output) {
					ClassFilePrinter printer = new ClassFilePrinter(cf.pool);
					printer.printNumber(cf.magicNumber, cf.majorVersion, cf.minorVersion);
//					printer.printConstantPool();
					printer.printAccessFlag(cf.accessFlag);
					printer.printThisClass(cf.thisClass);
					printer.printSuperClass(cf.superClass);
					printer.printInterface(cf.interfaces);
					printer.printFields(cf.fields);
					printer.printMethods(cf.methods);
//					printer.printAttributes(cf.attr);
				}
				ClassContent cc = new ClassContent(cf);
//			DecompileProcessor dp = new DecompileProcessor();
//			dp.process(cc);
			}
		} catch(Exception e) {}
	}
}