package sds.classfile.constantpool;

import static sds.classfile.constantpool.ConstantType.*;
import static sds.util.DescriptorParser.parse;
import static sds.util.DescriptorParser.removeJavaLangPrefix;

/**
 * This class is for extracting value from
 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}.
 * @author inagaki
 */
public class Utf8ValueExtractor {
    /**
     * returns value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 * which is refered by sub-class of
	 * {@link sds.classfile.constantpool.ConstantInfo <code>ConstantInfo</code>}
     * from index.
     * @param index constant-pool index
     * @param pool constant-pool
     * @return value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
     */
    public static String extract(int index, ConstantInfo[] pool) {
        return extract(pool[index - 1], pool);
    }

	/**
	 * returns value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 * which is refered by sub-class of
	 * {@link sds.classfile.constantpool.ConstantInfo <code>ConstantInfo</code>}.
	 * @param info constant info
	 * @param pool constant-pool
	 * @return value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 */
	public static String extract(ConstantInfo info, ConstantInfo[] pool) {
		if(info.getTag() == C_UTF8) {
			return ((Utf8Info)info).getValue();
		}
		switch(info.getTag()) {
			case C_INTEGER:
				return ""+((IntInfo)info).getValue();
			case C_FLOAT:
				return ""+((FloatInfo)info).getValue();
			case C_LONG:
				return ""+((LongInfo)info).getValue();
			case C_DOUBLE:
				return ""+((DoubleInfo)info).getValue();
			case C_CLASS:
				ClassInfo ci = (ClassInfo)info;
				return removeJavaLangPrefix(extract(pool[ci.getNameIndex()-1], pool).replace("/", "."));
			case C_STRING:
				StringInfo si = (StringInfo)info;
				return extract(pool[si.getStringIndex()-1], pool);
			case C_FIELDREF:
			case C_METHODREF:
			case C_INTERFACE_METHODREF:
				MemberRefInfo rmi = (MemberRefInfo)info;
				return extract(pool[rmi.getClassIndex()-1], pool) + "."
					 + extract(pool[rmi.getNameAndTypeIndex()-1], pool);
			case C_NAME_AND_TYPE:
				NameAndTypeInfo nati = (NameAndTypeInfo)info;
				return extract(pool[nati.getNameIndex()-1], pool) + "|"
					 + parse(extract(pool[nati.getDescIndex()-1], pool));
			case C_METHOD_HANDLE:
				MethodHandleInfo mhi = (MethodHandleInfo)info;
				return extract(pool[mhi.getRefIndex()-1], pool);
			case C_METHOD_TYPE:
				MethodTypeInfo mti = (MethodTypeInfo)info;
				return parse(extract(pool[mti.getDescIndex()-1], pool));
			case C_INVOKE_DYNAMIC:
				InvokeDynamicInfo idi = (InvokeDynamicInfo)info;
				return extract(pool[idi.getNameAndTypeIndex()-1], pool);
			default:
				throw new RuntimeException("invalid type: " + info);
		}
	}
}
