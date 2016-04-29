package sds.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for parsing member's descriptor.
 * @author inagaki
 */
public class DescriptorParser {
	private static final String objPattern  = "L[a-z\\\\.]*[0-9a-zA-Z_\\$\\.]+";

	/**
	 * returns parsed descriptor.
	 * @param desc descriptor
	 * @return parsed descriptor
	 */
	public static String parse(String desc) {
		String primPattern = "(B|\\[B|C|\\[C|D|\\[D|F|\\[F|V|I|\\[I"
								+ "|J|\\[J|S|\\[S|Z|\\[Z|\\(|\\))";
		String genericsPattern = "T[A-Z]";
		desc = desc.replace("/", ".");
		String obj = "(" + objPattern + "|\\[" + objPattern + ")";
		String gen = "(" + genericsPattern + "|\\[" + genericsPattern + ")";
		Matcher m = Pattern.compile(obj + "|" + primPattern + "|" + gen).matcher(desc);
		StringBuilder sb = new StringBuilder();
		while(m.find()) {
			String s = m.group();
			if(s.startsWith("[")) { // primitive array
				if(s.length() == 2) {
					sb.append(parseType(s.substring(1))).append("[]");
				} else { // object or generics array
					sb.append(s.subSequence(2, s.length())).append("[]");
				}
			} else if(s.startsWith("L") || s.startsWith("T")) { // object or generics
				sb.append(s.subSequence(1, s.length()));
			} else if(s.equals("V")) { // void
				sb.append(parseType(s));
			} else if(!parseType(s).equals("")) { // primitive
				sb.append(parseType(s));
			} else if(s.matches("\\(|\\)")) {
				sb.append(s);
				continue;
			} else {
				continue;
			}
			sb.append(",");
		}
		return sb.toString().substring(0, sb.toString().length()-1);
	}

	/**
	 * returns import classes from descriptor.
	 * @param desc descriptor
	 * @return import classes
	 */
	public static String[] parseImportClass(String desc) {
		Set<String> classes = new LinkedHashSet<>();
		desc = desc.replace("/", ".");
		Matcher m = Pattern.compile("(" + objPattern + ")").matcher(desc);
		while(m.find()) {
			String s = m.group();
			if(s.startsWith("L") && !s.matches("Ljava\\.lang\\.[A-Z][a-zA-Z\\.]+")) {
				classes.add(s.substring(1, s.length()));
			}
		}
		if(classes.isEmpty()) {
			return new String[0];
		}
		return classes.toArray(new String[0]);
	}

	private static String parseType(String head) {
		switch(head) {
			case "B": return "byte";
			case "C": return "char";
			case "D": return "double";
			case "F": return "float";
			case "I": return "int";
			case "J": return "long";
			case "S": return "short";
			case "Z": return "boolean";
			case "V": return "void";
			case "(":
			case ")": return "";
			default :
				System.out.println(">>> unknown type \""+head+"\".");
				return "";
		}
	}
}