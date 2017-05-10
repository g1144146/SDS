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
public class BootstrapMethods extends AttributeInfo {
    private String[] bsmRef;
    private String[][] bootstrapArgs;

    /**
     * constructor.
     * @param data classfile stream
     * @param pool constant-pool
     * @throws IOException 
     */
    public BootstrapMethods(ClassFileStream data, ConstantInfo[] pool) throws IOException {
        super(AttributeType.BootstrapMethods);
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

    /**
     * returns bootstrap method.
     * @return bootstrap method
     */
    public String[] getBSMRef() {
        return bsmRef;
    }
    
    /**
     * retunrs bootstrap method arguments.
     * @return bootstrap method arguments
     */
    public String[][] getBSMArgs() {
        return bootstrapArgs;
    }
}