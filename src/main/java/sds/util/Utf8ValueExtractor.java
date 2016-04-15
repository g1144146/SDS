package sds.util;

import sds.classfile.ConstantPool;
import sds.classfile.MemberInfo;
import sds.classfile.constantpool.ClassInfo;
import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.DoubleInfo;
import sds.classfile.constantpool.FloatInfo;
import sds.classfile.constantpool.IntegerInfo;
import sds.classfile.constantpool.InvokeDynamicInfo;
import sds.classfile.constantpool.LongInfo;
import sds.classfile.constantpool.MemberRefInfo;
import sds.classfile.constantpool.MethodHandleInfo;
import sds.classfile.constantpool.MethodTypeInfo;
import sds.classfile.constantpool.NameAndTypeInfo;
import sds.classfile.constantpool.StringInfo;
import sds.classfile.constantpool.Utf8Info;

import static sds.classfile.constantpool.ConstantType.*;

/**
 * This class is for extracting value from
 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}.
 * @author inagaki
 */
public class Utf8ValueExtractor {
	/**
	 * returns value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 * which is refered sub-class of
	 * {@link sds.classfile.constantpool.ConstantInfo <code>ConstantInfo</code>}.
	 * @param info constant info
	 * @param pool ponstant-pool
	 * @return value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 */
	public static String extract(ConstantInfo info, ConstantPool pool) {
		if(info.getTag() == C_UTF8) {
			return ((Utf8Info)info).getValue();
		}
		switch(info.getTag()) {
			case C_INTEGER:
				return ""+((IntegerInfo)info).getValue();
			case C_FLOAT:
				return ""+((FloatInfo)info).getValue();
			case C_LONG:
				return ""+((LongInfo)info).getValue();
			case C_DOUBLE:
				return ""+((DoubleInfo)info).getValue();
			case C_CLASS:
				ClassInfo ci = (ClassInfo)info;
				return extract(pool.get(ci.getNameIndex()-1), pool).replace("/", ".");
			case C_STRING:
				StringInfo si = (StringInfo)info;
				return extract(pool.get(si.getStringIndex()-1), pool);
			case C_FIELDREF:
			case C_METHODREF:
			case C_INTERFACE_METHODREF:
				MemberRefInfo rmi = (MemberRefInfo)info;
				return extract(pool.get(rmi.getClassIndex()-1), pool) + "."
					+ extract(pool.get(rmi.getNameAndTypeIndex()-1), pool);
			case C_NAME_AND_TYPE:
				NameAndTypeInfo nati = (NameAndTypeInfo)info;
				return extract(pool.get(nati.getNameIndex()-1), pool)
					+ DescriptorParser.parse(extract(pool.get(nati.getDescriptorIndex()-1), pool));
			case C_METHOD_HANDLE:
				MethodHandleInfo mhi = (MethodHandleInfo)info;
				return extract(pool.get(mhi.getReferenceIndex()-1), pool);
			case C_METHOD_TYPE:
				MethodTypeInfo mti = (MethodTypeInfo)info;
				return DescriptorParser.parse(extract(pool.get(mti.getDescriptorIndex()-1), pool));
			case C_INVOKE_DYNAMIC:
				InvokeDynamicInfo idi = (InvokeDynamicInfo)info;
				return extract(pool.get(idi.getNameAndTypeIndex()), pool);
			default:
				System.out.println("invalid type: " + info);
				return "";
		}
	}

	/**
	 * returns value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 * which is refered sub-class of
	 * {@link sds.classfile.MemberInfo <code>MemberInfo</code>}.
	 * @param info member's info
	 * @param pool ponstant-pool
	 * @return value of
	 * {@link sds.classfile.constantpool.Utf8Info <code>Utf8Info</code>}
	 */
	public static String extract(MemberInfo info, ConstantPool pool) {
		return DescriptorParser.parse(extract(pool.get(info.getDescriptorIndex()-1), pool))
			+ " " + extract(pool.get(info.getNameIndex()-1), pool);
	}
}