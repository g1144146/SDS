package sds.classfile.attributes.stackmap;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import sds.classfile.ClassFileStream;
import sds.classfile.attributes.AttributeInfo;
import sds.classfile.bytecode.OpcodeInfo;
import sds.classfile.constantpool.ConstantInfo;

import static sds.classfile.attributes.stackmap.StackMapFrameParser.parseFrame;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4">
 * StackMapTable Attribute</a>.
 * @author inagaki
 */
public class StackMapTable implements AttributeInfo {
    /**
     * entries of stack-map-table.
     */
    public final Map<Integer, Map<String, List<String>>> entries;

    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @param opcodes opcode sequence of method
     * @throws IOException 
     */
    public StackMapTable(ClassFileStream data, ConstantInfo[] pool, OpcodeInfo[] opcodes) throws IOException {
        StackMapFrame[] frames = new StackMapFrame[data.readShort()];
        StackMapFrameFactory factory = new StackMapFrameFactory();
        for(int i = 0; i < frames.length; i++) {
            frames[i] = factory.create(data);
        }
        entries = parseFrame(frames, pool, opcodes);
    }
}