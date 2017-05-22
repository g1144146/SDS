package sds.classfile.constantpool;

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
        if(info instanceof Utf8Info)         return ((Utf8Info)info).value;
        if(info instanceof NumberInfo)       return ((NumberInfo)info).number.toString();
        if(info instanceof StringInfo)       return extract(pool[((StringInfo)info).stringIndex - 1], pool);
        if(info instanceof MethodHandleInfo) return extract(pool[((MethodHandleInfo)info).refIndex - 1], pool);
        if(info instanceof MethodTypeInfo)   return parse(extract(pool[((MethodTypeInfo)info).descIndex - 1], pool));
        if(info instanceof ClassInfo) {
            ClassInfo ci = (ClassInfo)info;
            return removeJavaLangPrefix(extract(pool[ci.nameIndex - 1], pool).replace("/", "."));
        }
        if(info instanceof MemberRefInfo) {
            MemberRefInfo rmi = (MemberRefInfo)info;
            return extract(pool[rmi.classIndex - 1], pool) + "." + extract(pool[rmi.nameAndTypeIndex - 1], pool);
        }
        if(info instanceof NameAndTypeInfo) {
            NameAndTypeInfo nati = (NameAndTypeInfo)info;
            return extract(pool[nati.nameIndex - 1], pool) + "|" + parse(extract(pool[nati.descIndex - 1], pool));
        }
        if(info instanceof InvokeDynamicInfo) {
            InvokeDynamicInfo idi = (InvokeDynamicInfo)info;
            return extract(pool[idi.nameAndTypeIndex - 1], pool);
        }
        throw new RuntimeException("invalid type: " + info);
    }
}