package sds.util;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * This class is for debugging.
 * @author inagaki
 */
public class Printer {
    private static PrintStream out = System.out;

    public static void print(String value)   { out.print(value);   }
    public static void println(Object value) { out.println(value); }
    public static void println(String value) { out.println(value); }
    public static void println(int value)    { out.println(value); }
    public static void println(long value)   { out.println(value); }
    public static void println(float value)  { out.println(value); }
    public static void println(double value) { out.println(value); }
    public static void println(char value)   { out.println(value); }
    public static void println(Object[] values) { out.println(Arrays.toString(values)); }
}