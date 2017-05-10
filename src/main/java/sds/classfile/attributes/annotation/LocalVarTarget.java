package sds.classfile.attributes.annotation;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for localvar_target which
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.1">
 * target_info</a> union has.<br>
 * @author inagaki
 */
public class LocalVarTarget extends TargetInfo {
    private int[][] table;

    LocalVarTarget(ClassFileStream data) throws IOException {
        super(TargetInfoType.LocalVarTarget);
        this.table = new int[data.readShort()][3];
        for(int i = 0; i < table.length; i++) {
            table[i][0] = data.readShort();
            table[i][1] = data.readShort();
            table[i][2] = data.readShort();
        }
    }

    /**
     * returns local var table.<br>
     * when one of array index defines N, the array content is next:<br>
     * - table[N][0]: start pc<br>
     * - table[N][1]: length<br>
     * - table[N][2]: index
     * @return local var table
     */
    public int[][] getTable() {
        return table;
    }
}
