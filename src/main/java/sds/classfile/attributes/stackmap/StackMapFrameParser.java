package sds.classfile.attributes.stackmap;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.constantpool.ConstantInfo;

import static sds.util.DescriptorParser.parse;
import static sds.classfile.bytecode.OperandExtractor.extractOperand;
import static sds.classfile.constantpool.Utf8ValueExtractor.extract;

/**
 *
 * @author inagaki
 */
public class StackMapFrameParser {

    private static List<String> getBefore(Map<Integer, Map<String, List<String>>> stackMap,
    int before, String type) {
        if(stackMap.get(before) != null) {
            if(stackMap.get(before).get(type) != null) {
                return stackMap.get(before).get(type);
            }
        }
        return new ArrayList<>();
    }

    public static Map<Integer, Map<String, List<String>>> parseFrame(StackMapFrame[] frames,
    ConstantInfo[] pool, OpcodeInfo[] opcodes) {
        Map<Integer, Map<String, List<String>>> stackMap = new LinkedHashMap<>();
        int before = 0;
        for(StackMapFrame frame : frames) {
            Map<String, List<String>> map = new HashMap<>();
            List<String> local = getBefore(stackMap, before, "local");
            List<String> list = new ArrayList<>();
            int key = 0;
            switch(frame.getFrameType()) {
                case SameFrame:
                    map.put("stack", list);
                    map.put("local", local);
                    key = frame.getTag();
                    break;
                case SameLocals1StackItemFrame:
                    SameLocals1StackItemFrame sl = (SameLocals1StackItemFrame)frame;
                    list.add(parseVerification(sl.getStack(), pool, opcodes));
                    map.put("stack", list);
                    map.put("local", local);
                    key = frame.getTag() - 64;
                    break;
                case SameLocals1StackItemFrameExtended:
                    SameLocals1StackItemFrameExtended sle = (SameLocals1StackItemFrameExtended)frame;
                    list.add(parseVerification(sle.getStack(), pool, opcodes));
                    map.put("stack", list);
                    map.put("local", local);
                    key = sle.getOffset();
                    break;
                case ChopFrame:
                    ChopFrame cf = (ChopFrame)frame;
                    int deleteArg = 251 - cf.getTag();
                    int argCount = local.size();
                    for(int i = argCount-1; i > ((argCount-1) - deleteArg); i--) {
                        local.remove(i);
                    }
                    map.put("stack", list);
                    map.put("local", local);
                    key = cf.getOffset();
                    break;
                case SameFrameExtended:
                    SameFrameExtended sf = (SameFrameExtended)frame;
                    map.put("stack", list);
                    map.put("local", local);
                    key = sf.getOffset();
                    break;
                case AppendFrame:
                    AppendFrame af = (AppendFrame)frame;
                    VerificationTypeInfo[] afInfo = af.getLocals();
                    for(int i = 0; i < afInfo.length; i++) {
                        local.add(parseVerification(afInfo[i], pool, opcodes));
                    }
                    map.put("stack", list);
                    map.put("local", local);
                    key = af.getOffset();
                    break;
                case FullFrame:
                    FullFrame ff = (FullFrame)frame;
                    VerificationTypeInfo[] ffStackInfo = ff.getStacks();
                    String[] ffStack = new String[ffStackInfo.length];
                    for(int i = 0; i < ffStack.length; i++) {
                        ffStack[i] = parseVerification(ffStackInfo[i], pool, opcodes);
                    }
                    VerificationTypeInfo[] ffLocalsInfo = ff.getLocals();
                    String[] ffLocals = new String[ffLocalsInfo.length];
                    for(int i = 0; i < ffLocals.length; i++) {
                        ffLocals[i] = parseVerification(ffLocalsInfo[i], pool, opcodes);
                    }
                    List<String> ffStackList = new ArrayList<>(Arrays.asList(ffStack));
                    List<String> ffLocalsList = new ArrayList<>(Arrays.asList(ffLocals));
                    map.put("stack", ffStackList);
                    map.put("local", ffLocalsList);
                    key = ff.getOffset();
                    break;
                default:
                    break;
            }
            stackMap.put(key, map);
            before = key;
        }
        return stackMap;
    }

    private static String parseVerification(VerificationTypeInfo info, ConstantInfo[] pool, OpcodeInfo[] opcodes) {
        if(info instanceof ObjectVariableInfo) {
            ObjectVariableInfo ov = (ObjectVariableInfo)info;
            String value = extract(ov.getCPoolIndex(), pool);
            return value.startsWith("[") ? parse(value) : value;
        }
        if(info instanceof UninitializedVariableInfo) {
            UninitializedVariableInfo uv = (UninitializedVariableInfo)info;
            return extractOperand(opcodes[uv.getOffset()], pool);
        }
        return info.toString();
    }
}