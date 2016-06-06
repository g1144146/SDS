package sds.util;

import sds.classfile.ConstantPool;
import sds.classfile.attributes.annotation.Annotation;
import sds.classfile.attributes.annotation.ElementValue;
import sds.classfile.attributes.annotation.ElementValueException;
import sds.classfile.attributes.annotation.ElementValuePair;
import sds.classfile.attributes.annotation.EnumConstValue;

import static sds.util.DescriptorParser.parse;
import static sds.util.Utf8ValueExtractor.extract;

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
	 * @throws ElementValueException 
	 */
	public static String parseAnnotation(Annotation annotation, StringBuilder sb, ConstantPool pool)
	throws ElementValueException {
		sb.append("@").append(parse(extract(pool.get(annotation.getTypeIndex()-1), pool)))
			.append("(");
		ElementValuePair[] evp = annotation.getElementValuePairs();
		for(int i = 0; i < evp.length; i++) {
			sb.append(extract(pool.get(evp[i].getElementNameIndex()-1), pool))
				.append(" = ")
				.append(parseElementValue(evp[i].getValue(), new StringBuilder(), pool))
				.append("|");
		}
		return sb.toString().substring(0, sb.length()-1) + ")";
	}

	/**
	 * returns argument name and value which annotations has.
	 * @param element element value which annotations has
	 * @param sb  string builder for element value info
	 * @param pool constant-pool
	 * @return parsed element value
	 * @throws ElementValueException 
	 */
	public static String parseElementValue(ElementValue element, StringBuilder sb, ConstantPool pool)
	throws ElementValueException {
		switch(element.getTag()) {
			case 'B':
				sb.append(extract(pool.get(element.getConstValueIndex()-1), pool));
				break;
			case 'C':
				sb.append("'")
					.append(extract(pool.get(element.getConstValueIndex()-1), pool))
					.append("'");
				break;
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
				sb.append(extract(pool.get(element.getConstValueIndex()-1), pool));
				break;
			case 's':
				sb.append("\"")
					.append(extract(pool.get(element.getConstValueIndex()-1), pool))
					.append("\"");
				break;
			case 'e':
				EnumConstValue ecv = element.getEnumConstValue();
				sb.append(parse(extract(pool.get(ecv.getTypeNameIndex()-1), pool)))
					.append(".")
					.append(extract(pool.get(ecv.getConstNameIndex()-1), pool));
				break;
			case 'c':
				sb.append(parse(extract(pool.get(element.getClassInfoIndex()-1), pool)))
					.append(".class");
				break;
			case '@':
				sb.append(parseAnnotation(element.getAnnotationValue(), new StringBuilder(), pool));
				break;
			case '[':
				sb.append("{");
				StringBuilder values = new StringBuilder();
				for(ElementValue ev : element.getArrayValue().getValues()) {
					values.append(parseElementValue(ev, new StringBuilder(), pool))
						.append(",");
				}
				sb.append(values.toString().substring(0, sb.length()-1))
					.append("}");
				break;
			default:
				throw new ElementValueException(element.getTag());
		}
		return sb.toString();
	}
}