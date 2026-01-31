package sds.util;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * This class is for debugging.
 * @author inagaki
 */
public class Printer {
	private static PrintStream out = System.out;

	/**
	 * Prints a string value.
	 * @param value string to print
	 */
	public static void print(String value)   { out.print(value);   }
	/**
	 * Prints an object value with newline.
	 * @param value object to print
	 */
	public static void println(Object value) { out.println(value); }
	/**
	 * Prints a string value with newline.
	 * @param value string to print
	 */
	public static void println(String value) { out.println(value); }
	/**
	 * Prints an int value with newline.
	 * @param value int to print
	 */
	public static void println(int value)    { out.println(value); }
	/**
	 * Prints a long value with newline.
	 * @param value long to print
	 */
	public static void println(long value)   { out.println(value); }
	/**
	 * Prints a float value with newline.
	 * @param value float to print
	 */
	public static void println(float value)  { out.println(value); }
	/**
	 * Prints a double value with newline.
	 * @param value double to print
	 */
	public static void println(double value) { out.println(value); }
	/**
	 * Prints a char value with newline.
	 * @param value char to print
	 */
	public static void println(char value)   { out.println(value); }
	/**
	 * Prints an object array with newline.
	 * @param values object array to print
	 */
	public static void println(Object[] values) { out.println(Arrays.toString(values)); }
}
