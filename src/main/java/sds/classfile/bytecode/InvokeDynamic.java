package sds.classfile.bytecode;

import java.io.IOException;
import sds.classfile.ClassFileStream;
import sds.classfile.constantpool.ConstantInfo;

/**
 * This class is for
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokedynamic">
 * invokedynamic
 * </a>.
 * @author inagaki
 */
public class InvokeDynamic extends CpRefOpcode {
    InvokeDynamic(ClassFileStream data, ConstantInfo[] pool, int pc) throws IOException {
        super(data.readShort(), pool, MnemonicTable.inovokedynamic, pc);
        data.skipBytes(2);
    }
}