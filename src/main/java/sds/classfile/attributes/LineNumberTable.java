package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.12">
 * LineNumberTable Attribute</a>.
 * @author inagaki
 */
public class LineNumberTable extends AttributeInfo {
    private int[][] table;

    LineNumberTable(ClassFileStream data) throws IOException {
        super(AttributeType.LineNumberTable);
        this.table = new int[data.readShort()][3];
        for(int i = 0; i < table.length; i++) {
            table[i][0] = data.readShort();
            table[i][2] = data.readShort();
        }
        for(int i = 0; i < table.length - 1; i++) {
            table[i][1] = table[i + 1][0];
        }
        if(table.length > 1) table[table.length - 1][1] = table[table.length - 1][0];
    }

    /**
     * returns line number table.
     * when one of array index defines N, the array content is next:<br>
     * - table[N][0]: start pc<br>
     * - table[N][1]: end pc<br>
     * - table[N][2]: line number
     * @return line number table
     */
    public int[][] getLineNumberTable() {
        return table;
    }
}