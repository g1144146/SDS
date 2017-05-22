package sds.util;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for parsing member's descriptor.
 * @author inagaki
 */
public class DescriptorParser {
    private static final String objPattern  = "L[a-z\\.]*[0-9a-zA-Z_\\$\\.]+";
    private static final String lang = "java.lang.";
    private static String[] langPackages = new String[] {
        "java.lang.annotation",
        "java.lang.instrument",
        "java.lang.invoke",
        "java.lang.management",
        "java.lang.ref",
        "java.lang.reflect"
    };

    /**
     * returns parsed descriptor.
     * @param desc descriptor
     * @return parsed descriptor
     */
    public static String parse(String desc) {
        return parse(desc, false);
    }

    /**
     * returns parsed descriptor.
     * @param desc descriptor
     * @param isAttribute whether descriptor of attribute
     * @return parsed descriptor
     */
    public static String parse(String desc, boolean isAttribute) {
        String obj = "(" + objPattern + "|\\[+" + objPattern + ")";
        String primPattern = "(B|\\[+B|C|\\[+C|D|\\[+D|F|\\[+F|V|I|\\[+I|J|\\[+J|S|\\[+S|Z|\\[+Z|\\(|\\))";
        String genericsPattern = "T[A-Z]";
        String gen = "(" + genericsPattern + "|\\[+" + genericsPattern + ")";
        String colon = "(;:|::|:)"; // for Signature Attribute
        String wildcard = "(\\+|\\*)";
        String diamondOperator = "(<|>)";
        StringBuilder pattern = new StringBuilder();
        pattern.append(obj).append("|").append(primPattern).append("|")
                .append(gen).append("|").append(colon).append("|").append(wildcard)
                .append("|").append(diamondOperator).append("|([A-Z])|(;)");
        desc = desc.replace("/", ".").replace(";>", ">").replace(";)", ")");
        Matcher m = Pattern.compile(pattern.toString()).matcher(desc);
        StringBuilder sb = new StringBuilder();
        while(m.find()) {
            String s = m.group();
            if(s.startsWith("[")) { // array
                String type;
                if(s.matches(primPattern)) { // primitive array
                    // PRIM_TYPE,
                    type = parseType(s.substring(s.length() - 1));
                    // PRIM_TYPE
                    type = type.substring(0, type.length() - 1);
                } else {
                    // OBJ_TYPE
                    type = s.substring(s.lastIndexOf("[") + 2, s.length());
                    type = removeJavaLangPrefix(type);
                }
                sb.append(type);
                // TYPE[][]...
                for(int i = 0; i <= s.lastIndexOf("["); i++) {
                    sb.append("[]");
                }
            } else if(s.startsWith("L") || s.matches("T[A-Z]+")) { // object or generics
                String object = removeJavaLangPrefix(s.substring(1, s.length()));
                sb.append(object);
            } else if(s.matches("\\(|\\)|<|>")) { // parentheses and diamond operator
                sb.append(s);
            } else if(s.equals(";:")) { // generics contains some extended interfaces
                sb.append(" & ");
            } else if(s.matches("::|:")) { // generics contains extended class or interface
                sb.append(" extends ");
            } else if(s.equals("*")) { // wildcard
                sb.append("?");
            } else if(s.equals("+")) { // wildcard contains extended class
                sb.append("? extends ");
            } else if(s.equals(";")) { // separator
                sb.append(",");
            } else if(isAttribute && s.matches("[A-Z]")) { // generics type on Signature
                sb.append(s);
            } else if(!parseType(s).equals("")) { // primitive or void
                sb.append(parseType(s));
            }
        }
        String parsed = sb.toString().substring(0, sb.toString().length());
        if(parsed.endsWith(","))  parsed =  parsed.substring(0, parsed.length()-1);
        if(parsed.contains(",)")) parsed = parsed.replace(",)", ")");
        return parsed;
    }

    /**
     * returns class name which removed java.lang prefix in case of java.lang package class.
     * @param target class name
     * @return removed class name
     */
    public static String removeJavaLangPrefix(String target) {
        if(! target.startsWith(lang)) {
            return target;
        }
        for(String _package : langPackages) {
            if(target.startsWith(_package)) {
                return target;
            }
        }
        return target.replace(lang, "");
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
            case "B": return "byte,";
            case "C": return "char,";
            case "D": return "double,";
            case "F": return "float,";
            case "I": return "int,";
            case "J": return "long,";
            case "S": return "short,";
            case "Z": return "boolean,";
            case "V": return "void";
            default : return "";
        }
    }
}