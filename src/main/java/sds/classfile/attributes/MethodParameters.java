package sds.classfile.attributes;

import java.io.IOException;
import java.util.StringJoiner;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

import static sds.util.AccessFlags.get;

/**
  * This class is for
  * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.24">
  * MethodParameters Attribute</a>.
 * @author inagaki
 */
public class MethodParameters extends AttributeInfo {
    private String[][] params;

    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public MethodParameters(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.MethodParameters);
        this.params = new String[data.readByte()][2];
        for(int i = 0; i < params.length; i++) {
            int nameIndex = data.readShort();
            int accessFlag = data.readShort();
            params[i][0] = get(accessFlag, "local");
            params[i][1] = extract(nameIndex, pool);
        }
    }

    /**
     * returns method parameters.<br>
     * when one of array index defines N, the array content is next:<br>
     * - param[N][0]: name<br>
     * - param[N][1]: access flag
     * @return method parameters
     */
    public String[][] getParams() {
        return params;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for(String[] param : params) {
            sj.add(param[1] + param[0]);
        }
        return sj.toString();
    }
}