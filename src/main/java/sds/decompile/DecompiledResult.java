package sds.decompile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * This class is for decompiled source.
 * @author inagaki
 */
public class DecompiledResult {
	private String name;
	private List<String> source;
	private int indentLevel;
	/**
	 * for changing indent level.<br>
	 * increases indent level.
	 */
	public static final int INCREMENT =  1;
	/**
	 * for changing indent level.<br>
	 * decreases indent level.
	 */
	public static final int DECREMENT = -1;

	/**
	 * constructor.
	 * @param name source file name
	 */
	public DecompiledResult(String name) {
		this.name = name;
		this.source = new ArrayList<>();
		this.indentLevel = 0;
	}

	/**
	 * changes indent level.
	 * @param level change amount of indent level
	 */
	public void changeIndent(int level) {
		indentLevel += level;
		if(indentLevel < 0) {
			throw new IllegalStateException("indent level is minus.");
		}
	}

	/**
	 * changes indent level, and writes decompiled line of source.
	 * @param text line of source
	 * @param level indent level
	 */
	public void write(String text, int level) {
		changeIndent(level);
		write(text);
	}

	/**
	 * writes decompiled line of source.
	 * @param text line of source
	 */
	public void write(String text) {
		StringBuilder indent = new StringBuilder(indentLevel * 2);
		for(int i = 0; i < indentLevel; i++) {
			indent.append("\t");
		}
		source.add(indent.toString() + text);
	}

	/**
	 * writes curly bracket for end scope.
	 */
	public void writeEndScope() {
		write("}", DECREMENT);
	}

	/**
	 * saves decompiled source as java file.
	 */
	public void save() {
		String home = System.getProperty("user.home");
		String separator = System.getProperty("line.separator");
		Path path = Paths.get(home, "Desktop", "Decompiled" + name);
		try {
			Files.deleteIfExists(path);
			BufferedWriter writer = Files.newBufferedWriter(path, WRITE, CREATE);
			for(String line : source) {
				writer.write(line + separator);
			}
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String separator = System.getProperty("line.separator");
		for(String line : source) {
			sb.append(line).append(separator);
		}
		return sb.toString();
	}
}