package sds.classfile.attributes;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">
 * BootstrapMethods Attribute</a>.
 * @author inagaki
 */
public class BootstrapMethods implements AttributeInfo {
    /**
     * bootstrap method
     */
    public final String[] bsmRef;
    /**
     * bootstrap method arguments.
     */
    public final String[][] bootstrapArgs;

    BootstrapMethods(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        int len = data.readShort();
        this.bsmRef = new String[len];
        this.bootstrapArgs = new String[len][];
        for(int i = 0; i < len; i++) {
            this.bsmRef[i] = extract(data.readShort(), pool);
            String[] args = new String[data.readShort()];
            for(int j = 0; j < args.length; j++) {
                args[j] = extract(data.readShort(), pool);
            }
            this.bootstrapArgs[i] = args;
        }
    }
}