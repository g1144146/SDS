package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

import static sds.util.DescriptorParser.parse;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.13">
 * LocalVariableTable Attribute</a>.
 * and
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.14">
 * LocalVariableTypeTable Attribute</a>.
 * @author inagaki
 */
public class LocalVariable extends AttributeInfo {
    private int[][] table;
    private String[] name;
    private String[] desc;

    LocalVariable(AttributeType type, ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(type);
        int len = data.readShort();
        this.table = new int[len][3];
        this.name = new String[len];
        this.desc = new String[len];
        for(int i = 0; i < len; i++) {
            table[i][0] = data.readShort();
            table[i][1] = data.readShort();
            int nameIndex = data.readShort();
            int descIndex = data.readShort();
            table[i][2] = data.readShort();
            
            name[i] = extract(nameIndex, pool);
            desc[i] = (descIndex-1 >= 0) ? parse(extract(descIndex, pool)) : "";
        }
    }

    /**
     * return local variable table.<br>
     * when one of array index defines N, the array content is next:<br>
     * - table[N][0]: start pc<br>
     * - table[N][1]: length<br>
     * - table[N][2]: index
     * @return local variable table
     */
    public int[][] getTable() {
        return table;
    }

    /**
     * returns local variable names.
     * @return name
     */
    public String[] getName() {
        return name;
    }
    
    /**
     * returns local variable descriptors.
     * @return descriptor
     */
    public String[] getDesc() {
        return desc;
    }
}