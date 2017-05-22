package sds.util;

import static java.util.Arrays.stream;

/**
 * This class is for access flag.
 * @author inagaki
 */
public class AccessFlags {
    static final FlagPair PUBLIC       = new FlagPair(0x0001, "public "      );
    static final FlagPair PRIVATE      = new FlagPair(0x0002, "private "     );
    static final FlagPair PROTECTED    = new FlagPair(0x0004, "protected "   );
    static final FlagPair STATIC       = new FlagPair(0x0008, "static "      );
    static final FlagPair FINAL        = new FlagPair(0x0010, "final "       );
    static final FlagPair SYNCHRONIZED = new FlagPair(0x0020, "synchronized ");
    static final FlagPair VOLATILE     = new FlagPair(0x0040, "volatile "    );
    static final FlagPair TRANSIENT    = new FlagPair(0x0080, "transient "   );
    static final FlagPair NATIVE       = new FlagPair(0x0100, "native "      );
    static final FlagPair INTERFACE    = new FlagPair(0x0200, "interface "   );
    static final FlagPair ABSTRACT     = new FlagPair(0x0400, "abstract "    );
    static final FlagPair STRICT       = new FlagPair(0x0800, "strict "      );
    static final FlagPair SYNTHETIC    = new FlagPair(0x1000, "synthetic "   );
    static final FlagPair ANNOTATION   = new FlagPair(0x2000, "@interface "  );
    static final FlagPair ENUM         = new FlagPair(0x4000, "enum "        );
    static final int SUPER   = 0x0020;
    static final int BRIDGE  = 0x0040;
    static final int VARARGS = 0x0080;
    static final int CLASS  = PUBLIC.key   | FINAL.key     | SUPER          | INTERFACE.key |
                              ABSTRACT.key | SYNTHETIC.key | ANNOTATION.key | ENUM.key;
    static final int FIELD  = PUBLIC.key | PRIVATE.key  | PROTECTED.key | STATIC.key    |
                              FINAL.key  | VOLATILE.key | TRANSIENT.key | SYNTHETIC.key | ENUM.key;
    static final int METHOD = PUBLIC.key | PRIVATE.key | PROTECTED.key | STATIC.key   | FINAL.key  | SYNCHRONIZED.key |  
                              BRIDGE     | VARARGS     | NATIVE.key    | ABSTRACT.key | STRICT.key | SYNTHETIC.key;
    static final int NESTED = PUBLIC.key    | PRIVATE.key  | PROTECTED.key | STATIC.key     | FINAL.key |
                              INTERFACE.key | ABSTRACT.key | SYNTHETIC.key | ANNOTATION.key | ENUM.key;

    /**
     * returns access flags.
     * @param flag hex of access flag
     * @param type type name
     * @return access flags
     */
    public static String get(int flag, String type) {
        if(type.equals("class")  && checkOr(flag, CLASS))  return getClassAccessFlag(flag);
        if(type.equals("field")  && checkOr(flag, FIELD))  return getFieldAccessFlag(flag);
        if(type.equals("method") && checkOr(flag, METHOD)) return getMethodAccessFlag(flag);
        if(type.equals("nested") && checkOr(flag, NESTED)) return getClassAccessFlag(flag);
        if(type.equals("local")  && checkOr(flag, FINAL.key))    return (flag & FINAL.key) == FINAL.key ? "final " : "";
        throw new RuntimeException("unknown access flag: " + flag + ", type: " + type);
    }

    private static String getClassAccessFlag(int flag) {
        String before = build(flag, PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, ABSTRACT, SYNTHETIC);
        if((flag & ANNOTATION.key) == ANNOTATION.key) return before + ANNOTATION.value;
        if((flag & ENUM.key)       == ENUM.key)       return before + ENUM.value;
        if((flag & INTERFACE.key)  == INTERFACE.key)  return before + INTERFACE.value;
        return before + "class ";
    }

    private static String getFieldAccessFlag(int flag) {
        return build(flag, PUBLIC, PRIVATE,  PROTECTED, STATIC, FINAL, VOLATILE, TRANSIENT, SYNTHETIC, ENUM);
    }

    private static String getMethodAccessFlag(int flag) {
        return build(flag, PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SYNCHRONIZED, NATIVE, ABSTRACT, STRICT, SYNTHETIC);
    }

    private static String build(int flag, FlagPair... pairs) {
        return stream(pairs).filter((FlagPair pair) -> (flag & pair.key) == pair.key)
                            .map((FlagPair pair)    -> pair.value)
                            .reduce("", (String flag1, String flag2) -> flag1 + flag2);
    }

    private static boolean checkOr(int  flag, int target) { return (flag | target) == target; }
}

class FlagPair {
    final int key;
    final String value;
    FlagPair(int key, String value) {
        this.key   = key;
        this.value = value;
    }
}