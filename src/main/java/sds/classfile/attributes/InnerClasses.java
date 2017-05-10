package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

import static sds.util.AccessFlags.get;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.6">
 * InnerClasses Attribute</a>.
 * @author inagaki
 */
public class InnerClasses extends AttributeInfo {
    private String[][] classes;

    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public InnerClasses(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.InnerClasses);
        this.classes = new String[data.readShort()][4];
        for(int i = 0; i < classes.length; i++) {
            int innerClassInfoIndex = data.readShort();
            int outerClassInfoIndex = data.readShort();
            int innerNameIndex = data.readShort();
            int accessFlags = data.readShort();
            if(checkRange(innerClassInfoIndex-1, pool.length)) {
                classes[i][0] = extract(innerClassInfoIndex, pool);
            }
            if(checkRange(outerClassInfoIndex-1, pool.length)) {
                classes[i][1] = extract(outerClassInfoIndex, pool);
            }
            if(checkRange(innerNameIndex-1, pool.length)) {
                classes[i][2]  = extract(innerNameIndex, pool);
            }
            classes[i][3] = get(accessFlags, "nested");
        }
    }

    private boolean checkRange(int index, int size) {
        return (0 <= index) && (index < size);
    }

    /**
     * returns inner classes.<br>
     * when one of array index defines N, the array content is next:<br>
     * - classes[N][0]: inner class<br>
     * - classes[N][1]: outer class<br>
     * - classes[N][2]: inner class name<br>
     * - classes[N][3]: inner class access flag
     * @return classes
     */
    public String[][] getClasses() {
        return classes;
    }
}