package sophomore.classfile;

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author inagaki
 */
public class DescriptorParser {

	/**
	 * 
	 * @param type
	 * @param desc
	 * @return 
	 */
	public static String parse(String type, String desc) {
		if(type.equals("field")) {
			return parseField(desc);
		}
		return parseMethod(desc);
	}

	/**
	 * 
	 * @param desc
	 * @return 
	 */
	private static String parseField(String desc) {
		desc = desc.replace("/", ".");
		String[] descArray = desc.split("");
		if(descArray.length == 1) { // primitive
			return parseType(descArray[0]);
		} else if(descArray.length == 2) { // array primitive
			return parseType(descArray[1]) + parseType(descArray[0]);
		}
		boolean isArray = descArray[0].equals("[");
		String str = desc.substring(desc.indexOf("L")+1, desc.indexOf(";"));
		if(isArray) { // array object
			str += "[]";
		}
		return str;
	}

	/**
	 * 
	 * @param desc
	 * @return 
	 */
	private static String parseMethod(String desc) {
		desc = desc.replace("/", ".");
		StringBuilder sb = new StringBuilder();
		boolean isArray = false;
		Iterator<String> itr = Arrays.asList(desc.split("")).iterator();
		while(itr.hasNext()) {
			String str = itr.next();
			if(str.matches("\\(|\\)")) {
				sb.append(str);
			} else if(str.equals("[")) {
				isArray = true;
			} else {
				String type = parseType(str);
				if(type.equals("object")) {
					String s;
					while((s = itr.next()).equals(";")) {
						sb.append(s);
					}
				} else {
					sb.append(type);
				}
				if(isArray) {
					sb.append("[]");
					isArray = false;
				}
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param head
	 * @return 
	 */
	private static String parseType(String head) {
		switch(head) {
			case "B":
				return "byte";
			case "C":
				return "char";
			case "D":
				return "double";
			case "F":
				return "float";
			case "I":
				return "int";
			case "J":
				return "long";
			case "S":
				return "short";
			case "Z":
				return "boolean";
			case "L": // object
				return "object";
			default:
				System.out.println("Invalid head of string: " + head);
				return null;
		}
	}
}
