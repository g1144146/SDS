package sds.classfile.attributes.annotation;

import sds.classfile.constantpool.ConstantInfo;
import sds.util.SDSStringBuilder;

import static sds.util.DescriptorParser.parse;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 * This class is for parsing annotation.
 * @author inagaki
 */
public class AnnotationParser {

    /**
     * returns parsed annotation.<br>
     * parsed annotation format is next:<br>
     * ex1). @annotationClassName(arg1=value1, ...)<br>
     * ex2). @annotationClassName1(arg1=value1, ...)|@annotationClassName2(arg1=value1, ...)|...
     * @param annotation target annotation
     * @param sb string builder for annotation info
     * @param pool constant-pool
     * @return parsed annotation
     */
    public static String parseAnnotation(Annotation annotation, SDSStringBuilder sb, ConstantInfo[] pool) {
        sb.append("@", parse(extract(annotation.getTypeIndex(), pool)), "(");
        ElementValuePair[] evp = annotation.getElementValuePairs();
        for(int i = 0; i < evp.length; i++) {
            sb.append(extract(evp[i].getElementNameIndex(), pool), " = ", 
                parseElementValue(evp[i].getValue(), new SDSStringBuilder(), pool), ",");
        }
        return evp.length > 0 
                ? sb.toString().substring(0, sb.length()-1) + ")"
                : sb.toString().substring(0, sb.length()-1);
    }

    /**
     * returns argument name and value which annotations has.
     * @param element element value which annotations has
     * @param sb  string builder for element value info
     * @param pool constant-pool
     * @return parsed element value
     */
    public static String parseElementValue(ElementValue element, SDSStringBuilder sb, ConstantInfo[] pool) {
        switch(element.getTag()) {
            case 'B':
                sb.append(extract(element.getConstValueIndex(), pool));
                break;
            case 'C':
                sb.append("'", extract(element.getConstValueIndex(), pool), "'");
                break;
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                sb.append(extract(element.getConstValueIndex(), pool));
                break;
            case 's':
                sb.append("\"", extract(element.getConstValueIndex(), pool), "\"");
                break;
            case 'e':
                EnumConstValue ecv = element.getEnumConstValue();
                sb.append(parse(extract(ecv.getTypeNameIndex(), pool)), ".", extract(ecv.getConstNameIndex(), pool));
                break;
            case 'c':
                sb.append(parse(extract(element.getClassInfoIndex(), pool)), ".class");
                break;
            case '@':
                sb.append(parseAnnotation(element.getAnnotationValue(), new SDSStringBuilder(), pool));
                break;
            case '[':
                sb.append("{");
                SDSStringBuilder values = new SDSStringBuilder();
                for(ElementValue ev : element.getArrayValue().getValues()) {
                    values.append(parseElementValue(ev, new SDSStringBuilder(), pool), ",");
                }
                sb.append(values.toString().substring(0, values.length() - 1), "}");
                break;
            default:
                throw new RuntimeException(Character.toString(element.getTag()));
        }
        return sb.toString();
    }
}