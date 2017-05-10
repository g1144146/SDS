package sds.classfile;

import sds.classfile.constantpool.ConstantInfo;
import sds.classfile.constantpool.Utf8ValueExtractor;

/**
 * This interface is for info which classfile has.
 * @author inagaki
 */
public interface Info {
    default public String extract(int index, ConstantInfo[] pool) {
        return Utf8ValueExtractor.extract(pool[index - 1], pool);
    }
}